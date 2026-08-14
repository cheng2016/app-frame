package com.cds.iot.feature.device

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.ui.setupActionBar
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class ScanFragment : Fragment(R.layout.fragment_scan) {

    private val addViewModel: AddDeviceViewModel by viewModels()
    private val handled = AtomicBoolean(false)
    private val analysisExecutor = Executors.newSingleThreadExecutor()
    private var previewView: PreviewView? = null
    private var manualInput: AppCompatEditText? = null
    private var scanHint: TextView? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) startCamera() else {
            Toast.makeText(requireContext(), "需要相机权限才能扫码，可改用手动输入", Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.scan_qr)) { findNavController().navigateUp() }
        previewView = view.findViewById(R.id.previewView)
        manualInput = view.findViewById(R.id.manualInput)
        scanHint = view.findViewById(R.id.scanHint)
        view.findViewById<AppCompatButton>(R.id.submitButton).setOnClickListener {
            submitCode(manualInput?.text?.toString().orEmpty())
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED -> startCamera()
            else -> permissionLauncher.launch(Manifest.permission.CAMERA)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    addViewModel.message.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
                launch {
                    addViewModel.done.collect { findNavController().navigateUp() }
                }
            }
        }
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val previewView = previewView ?: return
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            if (!isAdded) return@addListener
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_AZTEC,
                    Barcode.FORMAT_CODE_128,
                    Barcode.FORMAT_EAN_13,
                )
                .build()
            val scanner = BarcodeScanning.getClient(options)
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            analysis.setAnalyzer(analysisExecutor) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage == null || handled.get()) {
                    imageProxy.close()
                    return@setAnalyzer
                }
                val image = InputImage.fromMediaImage(
                    mediaImage,
                    imageProxy.imageInfo.rotationDegrees,
                )
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        val value = barcodes.firstOrNull()?.rawValue
                        if (!value.isNullOrBlank() && handled.compareAndSet(false, true)) {
                            view?.post {
                                if (!isAdded) return@post
                                manualInput?.setText(value)
                                scanHint?.text = "已识别，正在添加…"
                                submitCode(value)
                            }
                        }
                    }
                    .addOnCompleteListener { imageProxy.close() }
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis,
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "相机启动失败：${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun submitCode(code: String) {
        val trimmed = code.trim()
        if (trimmed.isBlank()) {
            Toast.makeText(requireContext(), "请扫描或输入序列号", Toast.LENGTH_SHORT).show()
            handled.set(false)
            return
        }
        addViewModel.add(name = "扫码设备-$trimmed", type = "扫码添加")
    }

    override fun onDestroyView() {
        previewView = null
        manualInput = null
        scanHint = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        analysisExecutor.shutdown()
        super.onDestroy()
    }
}
