package com.example.dialogexample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dialogexample.databinding.FragmentUserBinding
import kotlinx.coroutines.*

class UserFragment : Fragment(){

    lateinit var binding:FragmentUserBinding

    private var imageUri: Uri? = null

    private lateinit var dialog: NewUserDialog


    //Here we receive the image.
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            imageUri = data!!.data

            dialog.setImage(imageUri!!)


        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater,container,false);

        binding.btnShowDialog.setOnClickListener {
            dialog = NewUserDialog(null, listener)
            dialog.show(requireActivity().supportFragmentManager, "NewUserDialogFragment")
        }

        binding.btnLoadUser.setOnClickListener {
            //Here we have to retrieve the user and show it in the dialog

            CoroutineScope(Dispatchers.IO).launch {
                val users = AppDatabase.getInstance(requireContext()).getAll()

                if(users.isNotEmpty()){
                    withContext(Dispatchers.Main){
                        //In this case we only want the first user.
                        dialog = NewUserDialog(users[0], listener)
                        dialog.show(requireActivity().supportFragmentManager, "UpdateUserDialogFragment")
                    }
                }else{
                    Toast.makeText(requireContext(),getString(R.string.no_user_saved), Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root;
    }





    //This handler is used for the Dialog
    val listener = object: NewUserDialog.NoticeDialogListener{

        override fun onDialogPositiveClick(userEntity: UserEntity) {
            //Here we have our user so we have to save it

            CoroutineScope(Dispatchers.IO).launch {
                val idLists = AppDatabase.getInstance(requireContext()).insertAll(userEntity)
                val id = idLists[0].toInt()
                //We save the image
                ImageController.saveImage(requireContext(),id, imageUri!!)
            }


            dialog.dismiss()




        }

        override fun onDialogNegativeClick() {
            TODO("Not yet implemented")
        }

        override fun onLoadImage() {
            //Here we have to call loadImage
            ImageController.selectPhotoFromGallery(resultLauncher)
        }
    }


}