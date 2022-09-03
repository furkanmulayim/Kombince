package com.example.kombince.view.fragmentview

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kombince.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class PostSheetFragment : BottomSheetDialogFragment() {


    private var chosedPngOne: Uri? = null
    private var chosedPngTwo: Uri? = null
    private var chosedBitmapOne: Bitmap? = null
    private var chosedBitmapTwo: Bitmap? = null
    private var click = true
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        return inflater.inflate(R.layout.fragment_post_sheet, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(
            requireContext(),
            "You can choose by clicking on the images",
            Toast.LENGTH_SHORT
        ).show()

        /** Implementation */
        var textDismiss = view?.findViewById(R.id.dismiss) as TextView
        var paylasButton = view?.findViewById(R.id.paylasButton) as Button
        var compareImgOne = view?.findViewById(R.id.compareImgOne) as ImageView
        var compareImgTwo = view?.findViewById(R.id.compareImgTwo) as ImageView


        textDismiss.setOnClickListener {
            dismiss()
        }

        /** Function to select the 1st  image */
        compareImgOne.setOnClickListener {
            click = true
            gorselSec()
        }

        /** select the 2nd image */
        compareImgTwo.setOnClickListener {
            click = false
            gorselSec()
        }

        /**share post */
        paylasButton.setOnClickListener {

            if (chosedPngOne != null && chosedPngTwo != null) {
                paylas()

            } else {
                Toast.makeText(requireContext(), "Lütfen iki  görsel seçiniz..", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun gorselSec() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 2)
        }
    }


     fun paylas() {

        var comment = view?.findViewById(R.id.comment) as EditText
        var uimageLink1 = ""
        var mailFolder = ""

        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            mailFolder = acct.email.toString()
        }

        /*******************************  Birinci Görsel İçin  ********************************/
        val reference = storage.reference
        var mailSubFolder = UUID.randomUUID().toString()
        val imageReferance = reference
            .child("Posts")
            .child("$mailFolder")
            .child("$mailSubFolder")
            .child("$mailSubFolder.png")
        if (chosedPngOne != null) {
            imageReferance.putFile(chosedPngOne!!).addOnSuccessListener { ts ->
                val ui = FirebaseStorage.getInstance().reference
                    .child("Posts")
                    .child("$mailFolder")
                    .child("$mailSubFolder")
                    .child("$mailSubFolder.png")

                ui.downloadUrl.addOnSuccessListener { uri ->
                    uimageLink1 = uri.toString()
                }
            }
        }
        /*******************************  İkinci Görsel İçin  ********************************/
        var mailSubFolder2 = UUID.randomUUID().toString()
        val imageReferance2 = reference
            .child("Posts")
            .child("$mailFolder")
            .child("$mailSubFolder")
            .child("$mailSubFolder2.png")

        if (chosedPngTwo != null) {
            imageReferance2.putFile(chosedPngTwo!!).addOnSuccessListener { ts ->
                val ui = FirebaseStorage.getInstance().reference
                    .child("Posts")
                    .child("$mailFolder")
                    .child("$mailSubFolder")
                    .child("$mailSubFolder.png")
                ui.downloadUrl.addOnSuccessListener { uri ->

                    /*******************************  Database İşlemleri  ********************************/
                    val uimageLink2 = uri.toString()
                    val upersonName = acct?.displayName.toString()
                    val uuserComment = comment.text.toString()
                    val udate = Timestamp.now()
                    val ulike1 = "0"
                    val ulike2 =  "0"
                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap.put("imageLink1", uimageLink1)
                    postHashMap.put("imageLink2", uimageLink2)
                    postHashMap.put("personName", upersonName)
                    postHashMap.put("userComment", uuserComment)
                    postHashMap.put("date", udate)
                    postHashMap.put("like1",ulike1)
                    postHashMap.put("like2",ulike2)

                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            dismiss()
                        }
                    }.addOnFailureListener { ex ->
                        Toast.makeText(
                            requireContext(),
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.addOnFailureListener { ex ->
                    Toast.makeText(requireContext(), ex.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var compareImgOne = view?.findViewById(R.id.compareImgOne) as ImageView
        var compareImgTwo = view?.findViewById(R.id.compareImgTwo) as ImageView
        if (click) { // 1. Gorselin butonu için
            if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
                chosedPngOne = data.data
                if (chosedPngOne != null) {
                    if (Build.VERSION.SDK_INT >= 28) {
                        var source =
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                chosedPngOne!!
                            )
                        chosedBitmapOne = ImageDecoder.decodeBitmap(source)
                        compareImgOne.setImageBitmap(chosedBitmapOne)
                    } else {
                        chosedBitmapOne = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            chosedPngOne
                        )
                        compareImgOne.setImageBitmap(chosedBitmapOne)
                    }
                }
            }
        } else {
            if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
                chosedPngTwo = data.data

                if (chosedPngTwo != null) {

                    if (Build.VERSION.SDK_INT >= 28) {
                        var source =
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                chosedPngTwo!!
                            )
                        chosedBitmapTwo = ImageDecoder.decodeBitmap(source)
                        compareImgTwo.setImageBitmap(chosedBitmapTwo)
                    } else {
                        chosedBitmapTwo = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            chosedPngTwo
                        )
                        compareImgTwo.setImageBitmap(chosedBitmapTwo)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

