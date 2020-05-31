package com.moonlightbutterfly.bookid


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moonlightbutterfly.bookid.repository.database.entities.Author
import com.moonlightbutterfly.bookid.repository.database.entities.Book


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, BookFragment.newInstance(
                Utils.convertToJSONString(Book(
                1,
                "Title",
                Author(2,"Author"),
                "pubdate",
            4.5,
            "url",
            "smallUrl"))))
            .commit()
    }
}
