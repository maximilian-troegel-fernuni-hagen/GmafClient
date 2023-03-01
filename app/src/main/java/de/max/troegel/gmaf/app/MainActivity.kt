package de.max.troegel.gmaf.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.max.troegel.gmaf.R
import de.max.troegel.gmaf.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (viewModel.shouldDisplayEndpointWarning()) {
            Toast.makeText(this, getString(R.string.no_endpoint_warning), Toast.LENGTH_LONG).show()
        }
        viewModel.handleIntent(intent, this)
    }
}