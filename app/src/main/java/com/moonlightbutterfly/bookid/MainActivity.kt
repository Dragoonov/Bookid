package com.moonlightbutterfly.bookid


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moonlightbutterfly.bookid.fragments.BookFragment
import com.moonlightbutterfly.bookid.fragments.ProfileFragment
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.fragment_container, ProfileFragment.newInstance())
//                .commit()
//        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container, BookFragment.newInstance(
                        Utils.convertToJSONString(
                            Book(
                                1,
                                "Title 1",
                                Author(18541, "pupa", "il"),
                                "33",
                                4.5,
                                "345"
                            )
                        )
                    )
                )
                .commit()
        }


    }
}
