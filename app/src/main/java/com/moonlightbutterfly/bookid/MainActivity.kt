package com.moonlightbutterfly.bookid


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moonlightbutterfly.bookid.fragments.BookFragment
import com.moonlightbutterfly.bookid.fragments.ProfileFragment
import com.moonlightbutterfly.bookid.fragments.ShelfFragment
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book
import com.moonlightbutterfly.bookid.repository.database.entities.Shelf
import com.moonlightbutterfly.bookid.repository.internalrepo.InternalRepository
import com.moonlightbutterfly.bookid.repository.internalrepo.RoomRepository


class MainActivity : AppCompatActivity() {

    val booksList = ArrayList<Book>().apply {
        add(
            Book(
                3,
                "title1", Author(1, "author1", "imageurl"), "pubdate", 1.2, "image"
            )
        )
        add(
            Book(
                3,
                "title2", Author(2, "author2", "imageurl"), "pubdate", 1.2, "image"
            )
        )
        add(
            Book(
                3,
                "title3", Author(3, "author3", "imageurl"), "pubdate", 1.2, "image"
            )
        )
    }
    val shelfListMock = ArrayList<Shelf>().apply {
        add(
            Shelf(
                1,
                "Polka1",
                booksList,
                1
            )
        )
        add(
            Shelf(
                2,
                "Polka2",
                booksList,
                1
            )
        )
        add(
            Shelf(
                3,
                "Polka3",
                booksList,
                1
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.fragment_container, ProfileFragment.newInstance())
//                .commit()
//        }
//
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ShelfFragment.newInstance())
                .commit()
        }

//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(
//                    R.id.fragment_container, BookFragment.newInstance(
//                        Book(
//                            1,
//                            "Title 1",
//                            Author(18541, "pupa", "il"),
//                            "33",
//                            4.5,
//                            "345"
//                        )
//                    )
//                )
//                .commit()
//        }


    }
}
