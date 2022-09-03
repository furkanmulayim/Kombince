package com.example.kombince.view.fragmentview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kombince.R
import com.example.kombince.view.model.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*


class FeedFragment : Fragment() {

    lateinit var database: FirebaseFirestore
    var postListesi = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        database = FirebaseFirestore.getInstance()

        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var FloatingActionButton = view.findViewById(R.id.flooo) as FloatingActionButton

        FloatingActionButton.setOnClickListener {
            val objectt = PostSheetFragment()
            activity?.supportFragmentManager?.let {
                objectt.show(it, "PostSheetFragment")
            }
        }

        getData()

    }


    fun getData() {

        var getUserName = ""
        var getComment = ""
        var getLink1 = ""
        var getLink2 = ""
        var getLike1 = ""
        var getLike2 = ""
        var getDate: Timestamp? = null

        var nowTime: String = ""

        database.collection("Post").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, ex ->
                if (ex != null) {
                    Toast.makeText(requireContext(), ex.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    if (snap != null) {
                        val documents = snap.documents


                        postListesi.clear()
                        for (doc in documents) {
                            getUserName = doc.get("personName") as String
                            getComment = doc.get("userComment") as String
                            getLink1 = doc.get("imageLink1") as String
                            getLink2 = doc.get("imageLink2") as String
                            getLike1 = doc.get("like1") as String
                            getLike2 = doc.get("like2") as String
                            getDate = doc.get("date") as Timestamp

                            val sdf = SimpleDateFormat(" dd MMM HH:mm", Locale.getDefault())
                            nowTime = sdf.format(Date((getDate?.seconds ?: 0) * 1000))

                            val downPost = Post(
                                getUserName,
                                getComment,
                                getLink1,
                                getLink2,
                                getLike1,
                                getLike2,
                                nowTime
                            )
                            postListesi.add(downPost)
                        }


                    }
                }
            }
    }


}