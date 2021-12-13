package com.ducnn17.movieCGV

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

import java.io.FileNotFoundException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.ducnn17.movieCGV.receiver.Constant
import com.ducnn17.movieCGV.receiver.PermissionUtils


interface ProfileEditListener {

}

class ProfileEditFragment : Fragment() {

    private val mProfileEditListener: ProfileEditListener? = null
    private var mBtnCancel: Button? = null
    private var mBtnDone: Button? = null
    private var mIvAvatar: ImageView? = null
    private var mEdtName: EditText? = null
    private var mEdtEmail: EditText? = null
    private var mTvBirthday: TextView? = null
    private var mRadioMale: RadioButton? = null
    lateinit var mBitmap: Bitmap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_edit, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBtnCancel = view.findViewById(R.id.btn_cancel_edit)
        mBtnDone = view.findViewById(R.id.btn_done_edit)
        mIvAvatar = view.findViewById(R.id.edit_imgAvatar)
        mEdtName = view.findViewById(R.id.edit_name)
        mEdtEmail = view.findViewById(R.id.edit_email)
        mTvBirthday = view.findViewById(R.id.edit_dob)
        mRadioMale = view.findViewById(R.id.radioButton)

        mIvAvatar!!.setOnClickListener {
            if (PermissionUtils.checkPermission(context, Constant.PERMISSION)) {
                selectImage()
            } else {
                PermissionUtils.requestPermission(activity, Constant.PERMISSION, Constant.REQUEST_CODE_PERMISSION)
            }
        }

        mTvBirthday!!.setOnClickListener {
            val value = arrayOf(Date())
            val calendar: Calendar = Calendar.getInstance();
            calendar.time = value[0]
            DatePickerDialog(
                requireActivity(),
                { _, y, m, d ->
                    calendar[Calendar.YEAR] = y
                    calendar[Calendar.MONTH] = m
                    calendar[Calendar.DAY_OF_MONTH] = d
                    val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    mTvBirthday!!.text = formatDate.format(calendar.time)
                }, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        /*mBtnCancel!!.setOnClickListener(){
            mProfileEditListener.onProfileCancel()
        }*/

        mBtnDone!!.setOnClickListener{

        }
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo")
        builder.setItems(options) { dialog, items ->
            run {
                if (options[items] == "Take Photo") {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent , Constant.REQUEST_CODE_CAMERA)
                }else if(options[items] == "Choose from Gallery" ){
                    val intent = Intent(Intent.ACTION_PICK , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent , Constant.REQUEST_CODE_PICK)
                }else if(options[items] == "Cancel"){
                    dialog.dismiss()
                }
            }
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK){
            when(requestCode){
                Constant.REQUEST_CODE_PERMISSION ->{
                    selectImage()
                }
                Constant.REQUEST_CODE_CAMERA ->{
                    val extras : Bundle? =data!!.extras
                    if (extras != null) {
                        mBitmap = extras.get("data") as Bitmap
                    }
                    mIvAvatar?.setImageBitmap(mBitmap)
                }

                Constant.REQUEST_CODE_PICK ->{
                    val imgUri: Uri = data?.data!!
                    val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? = context?.contentResolver?.query(imgUri , filePathColumns ,
                        null,null,null)
                    cursor?.moveToFirst()

                    val columnId = cursor?.getColumnIndex(filePathColumns[0])
                    val imgPath : String? = columnId?.let { cursor.getString(it) }
                    cursor?.close()
                    mBitmap = BitmapFactory.decodeFile(imgPath)
                    mIvAvatar?.setImageBitmap(mBitmap)
                }
            }
        }
    }

}