package com.example.dialogexample

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class NewUserDialog(var user:UserEntity?, val listener:NoticeDialogListener): DialogFragment() {


    private lateinit var layout: View

    interface NoticeDialogListener {
        fun onDialogPositiveClick(userEntity: UserEntity)
        fun onDialogNegativeClick()
        fun onLoadImage()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            layout = inflater.inflate(R.layout.dialog_new_user,null)

            layout.findViewById<ImageButton>(R.id.btn_load_file).setOnClickListener {
                listener.onLoadImage()
            }

            user?.let {  user ->
                val firstNameET: EditText = layout.findViewById(R.id.first_name)
                firstNameET.setText(user.firstName)

                val lastNameET: EditText = layout.findViewById(R.id.last_name)
                lastNameET.setText(user.lastName)

                val userIV: ImageView = layout.findViewById(R.id.ivUser)
                userIV.setImageURI(ImageController.getImageUri(requireContext(),user.uid!!))
            }

            layout.findViewById<Button>(R.id.btn_dialog_ok).setOnClickListener {

                val user = UserEntity(null,
                    layout.findViewById<EditText>(R.id.first_name).text.toString(),
                    layout.findViewById<EditText>(R.id.last_name).text.toString(),
                )
                listener.onDialogPositiveClick(user)
            }

            layout.findViewById<Button>(R.id.btn_dialog_cancel).setOnClickListener {

                dismiss()
            }


            builder.setView(layout)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setImage(uri: Uri){
        val userIV: ImageView = layout.findViewById(R.id.ivUser)
        userIV.setImageURI(uri)
    }
}