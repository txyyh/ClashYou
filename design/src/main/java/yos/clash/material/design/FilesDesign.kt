package yos.clash.material.design

import android.app.Dialog
import android.content.Context
import android.view.View
import yos.clash.material.design.adapter.FileAdapter
import yos.clash.material.design.databinding.DesignFilesBinding
import yos.clash.material.design.databinding.DialogFilesMenuBinding
import yos.clash.material.design.dialog.AppBottomSheetDialog
import yos.clash.material.design.dialog.requestModelTextInput
import yos.clash.material.design.model.File
import yos.clash.material.design.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilesDesign(context: Context) : Design<FilesDesign.Request>(context) {
    sealed class Request {
        data class OpenFile(val file: File) : Request()
        data class OpenDirectory(val file: File) : Request()
        data class RenameFile(val file: File) : Request()
        data class DeleteFile(val file: File) : Request()
        data class ImportFile(val file: File?) : Request()
        data class ExportFile(val file: File) : Request()

        object PopStack : Request()
    }

    private val binding = DesignFilesBinding
        .inflate(context.layoutInflater, context.root, false)
    private val adapter: FileAdapter = FileAdapter(context, this::requestOpen, this::requestMore)

    override val root: View
        get() = binding.root

    var configurationEditable: Boolean
        get() = binding.configurationEditable
        set(value) {
            binding.configurationEditable = value
        }

    suspend fun swapFiles(files: List<File>, currentInBaseDir: Boolean) {
        withContext(Dispatchers.Main) {
            adapter.swapDataSet(adapter::files, files)
            binding.currentInBaseDir = currentInBaseDir
        }
    }

    fun updateElapsed() {
        adapter.updateElapsed()
    }

    suspend fun requestFileName(name: String): String {
        return context.requestModelTextInput(
            initial = name,
            title = context.getText(R.string.file_name),
            hint = context.getText(R.string.file_name),
            error = context.getText(R.string.invalid_file_name),
            validator = ValidatorFileName,
        )
    }

    init {
        binding.self = this

        binding.activityBarLayout.applyFrom(context)

        binding.mainList.recyclerList.also {
            it.applyLinearAdapter(context, adapter)
            it.bindAppBarElevation(binding.activityBarLayout)
        }
    }

    private fun requestOpen(file: File) {
        if (file.isDirectory) {
            requests.trySend(Request.OpenDirectory(file))
        } else {
            requests.trySend(Request.OpenFile(file))
        }
    }

    fun requestRename(dialog: Dialog, file: File) {
        requests.trySend(Request.RenameFile(file))

        dialog.dismiss()
    }

    fun requestImport(dialog: Dialog, file: File) {
        requests.trySend(Request.ImportFile(file))

        dialog.dismiss()
    }

    fun requestExport(dialog: Dialog, file: File) {
        requests.trySend(Request.ExportFile(file))

        dialog.dismiss()
    }

    fun requestDelete(dialog: Dialog, file: File) {
        requests.trySend(Request.DeleteFile(file))

        dialog.dismiss()
    }

    fun requestNew() {
        requests.trySend(Request.ImportFile(null))
    }

    private fun requestMore(file: File) {
        val dialog = AppBottomSheetDialog(context)

        val binding = DialogFilesMenuBinding.inflate(context.layoutInflater)

        binding.master = this
        binding.self = dialog
        binding.file = file
        binding.currentInBase = this.binding.currentInBaseDir
        binding.configurationEditable = this.binding.configurationEditable

        dialog.setContentView(binding.root)
        dialog.show()
    }
}
