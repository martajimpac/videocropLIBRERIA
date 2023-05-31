package com.toools.videocrop

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.toools.videocrop.databinding.VideocropDialogThreeButtonsBinding


/**
 * A simple [Fragment] subclass.
 * Use the [DialogThreeButtonsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialogThreeButtonsFragment (private val imgDefault : Int?, private val  backgroundColor : Int?,
                                  private val iconThinColor : Int?, private val iconBackgroundColor : Int?, private val textColor : Int?,
                                  private val buttonColor : Int?, private val titleFont: Typeface?, private val descriptionFont: Typeface?,
                                  private val buttonFont: Typeface?, private val buttonTextColor: Int?): DialogFragment()  {

    private var act: Activity? = null
    private lateinit var binding: VideocropDialogThreeButtonsBinding

    private var title: Any? = null
    private var text: Any? = null
    private var icon: Int? = null
    private var completion1: (() -> Unit)? = null
    private var completion2: (() -> Unit)? = null
    private var completion3: (() -> Unit)? = null
    private var button1: Any? = null
    private var button2: Any? = null
    private var button3: Any? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VideocropDialogThreeButtonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity)
            act = context
    }

    private fun setUpView() {
        binding.apply {
            act?.let { context ->


                //titulo
                (title as? String)?.let {
                    tituloTextView.text = it
                }
                (title as? Int)?.let {
                    tituloTextView.text = context.getString(it)
                }

                titleFont?.let { font ->
                    tituloTextView.typeface = font
                }

                //Description
                (text as? String)?.let {
                    descripcionTextView.text = it
                }
                (text as? Int)?.let {
                    descripcionTextView.text = context.getString(it)
                }

                descriptionFont?.let { font ->
                    descripcionTextView.typeface = font
                }


                textColor?.let { color ->
                    tituloTextView.setTextColor(ContextCompat.getColor(context, color))
                    descripcionTextView.setTextColor(ContextCompat.getColor(context, color))
                    threeButtonTopTextView.setTextColor(ContextCompat.getColor(context, color))
                    threeButtonMiddleTextView.setTextColor(ContextCompat.getColor(context, color))
                    threeButtonBottomTextView.setTextColor(ContextCompat.getColor(context, color))
                }

                buttonColor?.let {color ->
                    threeButtonBottomCardView.setCardBackgroundColor(ContextCompat.getColor(context, color))
                    threeButtonTopCardView.setCardBackgroundColor(ContextCompat.getColor(context, color))
                    threeButtonMiddleCardView.setCardBackgroundColor(ContextCompat.getColor(context, color))
                }

                buttonFont?.let { font ->
                    threeButtonTopTextView.typeface = font
                    threeButtonMiddleTextView.typeface = font
                    threeButtonBottomTextView.typeface = font
                }

                buttonTextColor?.let { textColor ->
                    threeButtonTopTextView.setTextColor(ContextCompat.getColor(context, textColor))
                    threeButtonMiddleTextView.setTextColor(ContextCompat.getColor(context, textColor))
                    threeButtonBottomTextView.setTextColor(ContextCompat.getColor(context, textColor))
                }

                //background
                backgroundColor?.let { color ->
                    baseCardView.setCardBackgroundColor(ContextCompat.getColor(context, color))
                }

                //iconBackgroundColor
                iconBackgroundColor?.let { color ->
                    iconCardView.setCardBackgroundColor(ContextCompat.getColor(context, color))
                }

                //iconImage
                icon?.let {imagen ->
                    Glide.with(context.baseContext).load(imagen).into(dialogImageView)
                } ?: run{
                    imgDefault?.let { imagen ->
                        Glide.with(context.baseContext).load(imagen).into(dialogImageView)
                    }
                }

                iconThinColor?.let { color ->
                    dialogImageView.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN)
                }

                //button1
                (button1 as? String)?.let {
                    threeButtonTopTextView.text = it
                }
                (button1 as? Int)?.let {
                    threeButtonTopTextView.text = context.getString(it)
                }

                //button2
                (button2 as? String)?.let {
                    threeButtonMiddleTextView.text = it
                }
                (button2 as? Int)?.let {
                    threeButtonMiddleTextView.text = context.getString(it)
                }

                //button3
                (button3 as? String)?.let {
                    threeButtonBottomTextView.text = it
                }
                (button3 as? Int)?.let {
                    threeButtonBottomTextView.text = context.getString(it)
                }


                threeButtonTopCardView.setOnClickListener {
                    completion1?.let { it1 -> it1() }
                    dismiss()
                }

                //completion2
                threeButtonMiddleCardView.setOnClickListener {
                    completion2?.let { it1 -> it1() }
                    dismiss()
                }

                //completion3
                threeButtonBottomCardView.setOnClickListener {
                    completion3?.let { it1 -> it1() }
                    dismiss()
                }
            }
        }
    }

    fun showThreeButtons(title: Any? = null, text: Any, icon: Int? = imgDefault, button1: Any, completion1: (() -> Unit)? = null, button2: Any?, completion2: (() -> Unit)? = null, button3: Any?, completion3: (() -> Unit)? = null) {
        loadData(title = title, text = text, icon = icon, button1 = button1, completion1 = completion1, button2 = button2, completion2 = completion2, button3 = button3, completion3 = completion3)
    }

    fun loadData(title: Any? = null, text: Any, icon: Int? = imgDefault, button1: Any, completion1: (() -> Unit)? = null, button2: Any? = null, completion2: (() -> Unit)? = null, button3: Any? = null, completion3: (() -> Unit)? = null) {
        this.title = title
        this.text = text
        this.icon = icon
        this.button1 = button1
        this.completion1 = completion1
        this.button2 = button2
        this.completion2 = completion2
        this.button3 = button3
        this.completion3 = completion3
    }
}