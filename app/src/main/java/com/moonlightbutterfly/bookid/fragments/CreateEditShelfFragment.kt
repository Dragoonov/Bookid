package com.moonlightbutterfly.bookid.fragments

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.moonlightbutterfly.bookid.R
import com.moonlightbutterfly.bookid.databinding.FragmentCreateEditShelfBinding
import com.moonlightbutterfly.bookid.utils.Logos
import com.moonlightbutterfly.bookid.utils.provideLogoId
import com.moonlightbutterfly.bookid.viewmodels.CreateEditShelfViewModel

class CreateEditShelfFragment :
    BaseFragment<FragmentCreateEditShelfBinding, CreateEditShelfViewModel>(CreateEditShelfViewModel::class.java) {

    private val args: CreateEditShelfFragmentArgs by navArgs()

    val listener: (id: Int) -> View.OnClickListener = { id ->
        View.OnClickListener {
            viewModel.iconId = it.provideLogoId(id)
            binding?.front?.setImageDrawable(ContextCompat.getDrawable(requireContext(), id))
        }
    }

    override fun inject() = appComponent.inject(this)

    override fun initializeBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentCreateEditShelfBinding.inflate(inflater, container, false).apply {
            viewModel = this@CreateEditShelfFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
            ok.setOnClickListener {
                val baseColor = (binding!!.base.background as ColorDrawable).color
                this@CreateEditShelfFragment.viewModel.finishCreateModify(
                    binding!!.name.text.toString(),
                    baseColor,
                    this@CreateEditShelfFragment.viewModel.iconId
                )
                findNavController().popBackStack()
            }
            fish.setOnClickListener(listener(R.drawable.ic_fish))
            internet.setOnClickListener(listener(R.drawable.ic_internet))
            ball.setOnClickListener(listener(R.drawable.ic_soccer))
            science.setOnClickListener(listener(R.drawable.ic_science))
            rubics.setOnClickListener(listener(R.drawable.ic_rubiks_cube))
            viewModel!!.shelfLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    binding?.apply {
                        name.setText(it.name)
                        base.setBackgroundColor(it.cover.background)
                        front.setImageDrawable(ContextCompat.getDrawable(requireContext(), front.provideLogoId(it.cover.icon)))
                    }
                }
            })
        }
    }

    override fun initializeViewModel() {
        super.initializeViewModel()
        viewModel.apply {
            setShelfId(args.shelfId)
            setActionTitle(args.actionTitle)
            iconId = Logos.BALL
            errorOccurredMessage = requireContext().getString(R.string.error_occurred)
        }
    }
}