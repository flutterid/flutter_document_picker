package com.sidlatau.flutterdocumentpicker

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

class FlutterDocumentPickerPlugin(
        private val delegate: FlutterDocumentPickerDelegate
) : MethodCallHandler {
    companion object {
        const val TAG = "flutter_document_picker"
        private const val ARG_ALLOWED_FILE_EXTENSIONS = "allowedFileExtensions"
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_document_picker")

            val delegate = FlutterDocumentPickerDelegate(
                    activity = registrar.activity()
            )

            registrar.addActivityResultListener(delegate)

            channel.setMethodCallHandler(
                    FlutterDocumentPickerPlugin(delegate)
            )
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "pickDocument") {
            delegate.pickDocument(result, allowedFileExtensions = parseExtensionArg(call))
        } else {
            result.notImplemented()
        }
    }

    private fun parseExtensionArg(call: MethodCall): ArrayList<String>? {
        if (call.hasArgument(ARG_ALLOWED_FILE_EXTENSIONS)) {
            return  call.argument<ArrayList<String>>(ARG_ALLOWED_FILE_EXTENSIONS)
        }
        return null
    }
}
