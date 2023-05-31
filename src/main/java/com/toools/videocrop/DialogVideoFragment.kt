package com.toools.videocrop

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.video.VideoSize
import com.otaliastudios.zoom.ZoomSurfaceView
import com.toools.videocrop.databinding.VideocropDialogVideoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DialogVideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialogVideoFragment (private val  backgroundColor : Int?, private val textColor : Int?,
                           private val buttonColor : Int?, private val titleFont: Typeface?,
                           private val buttonFont: Typeface?, private val buttonTextColor: Int?,
                           private val exoplayerFont: Typeface?, private val iconPlay: Int?, private val iconPause: Int?): DialogFragment() {

    private var act: Activity? = null
    private lateinit var binding: VideocropDialogVideoBinding

    private var title: Any? = null
    private var clippingMediaSource: ClippingMediaSource? = null
    private var button1: Any? = null
    private var button2: Any? = null
    private var player: Player? = null
    private var recortePlayer: ExoPlayer? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VideocropDialogVideoBinding.inflate(inflater, container, false)
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

                textColor?.let { color ->
                    tituloTextView.setTextColor(ContextCompat.getColor(context, color))
                    twoButtonCancelTextView.setTextColor(ContextCompat.getColor(context, color))
                    twoButtonSuccessTextView.setTextColor(ContextCompat.getColor(context, color))
                }

                buttonColor?.let {color ->
                    twoButtonCancelCardView.setCardBackgroundColor(ContextCompat.getColor(context, color))
                    twoButtonSuccessCardView.setCardBackgroundColor(ContextCompat.getColor(context, color))
                }

                buttonFont?.let { font ->
                    twoButtonCancelTextView.typeface = font
                    twoButtonSuccessTextView.typeface = font
                }

                buttonTextColor?.let { textColor ->
                    twoButtonSuccessTextView.setTextColor(ContextCompat.getColor(context, textColor))
                    twoButtonCancelTextView.setTextColor(ContextCompat.getColor(context, textColor))
                }

                //background
                backgroundColor?.let { color ->
                    recorteCardView.setCardBackgroundColor(ContextCompat.getColor(context, color))
                }

                //button1
                (button1 as? String)?.let {
                    twoButtonSuccessTextView.text = it
                }
                (button1 as? Int)?.let {
                    twoButtonSuccessTextView.text = context.getString(it)
                }

                //button2
                (button2 as? String)?.let {
                    twoButtonCancelTextView.text = it
                }
                (button2 as? Int)?.let {
                    twoButtonCancelTextView.text = context.getString(it)
                }

                //personalizar exoplayercontrol
                Glide.with(context.baseContext).load(iconPlay).into(videocropPlayerControl.exoPlay)
                Glide.with(context.baseContext).load(iconPause).into(videocropPlayerControl.exoPause)

                exoplayerFont?.let{
                    videocropPlayerControl.exoPosition.typeface = exoplayerFont
                    videocropPlayerControl.textView.typeface = exoplayerFont
                    videocropPlayerControl.exoDuration.typeface = exoplayerFont
                    videocropPlayerControl.exoTextSpeed.typeface = exoplayerFont
                }
                videocropPlayerControl.exoPosition.text = "Probandoooooooooooooo"

                //reproducir el video
                recorteSurfaceView.onResume()
                recortePlayerView.player = recortePlayer

                recorteSurfaceView.setOnClickListener {
                    if (recortePlayerView.isVisible) {
                        recortePlayerView.hide()
                    } else {
                        recortePlayerView.show()
                    }
                }

                recorteSurfaceView.addCallback(zoomCallback)

                recortePlayer?.addListener(object : Player.Listener{
                    override fun onVideoSizeChanged(videoSize: VideoSize) {
                        recorteSurfaceView.setContentSize(videoSize.width.toFloat(), videoSize.height.toFloat())
                    }
                })

                recortePlayer?.let {
                    clippingMediaSource?.let { it1 -> it.setMediaSource(it1) }
                    it.prepare()
                    it.playWhenReady = true
                }

                //guardar
                twoButtonSuccessCardView.setOnClickListener(){
                    VideoCropHelper.guardarVideo()
                }

                //cancelar
                twoButtonCancelCardView.setOnClickListener(){
                    recortePlayer?.pause()
                    recorteSurfaceView.onPause()
                    recorteSurfaceView.visibility = View.GONE
                    dialogRecorteConstraintLayout.visibility = View.GONE

                    //reaunudar el player principal
                    player?.play()
                    dismiss()
                }
            }
        }
    }

    fun showVideo(title: Any? = null,clippingMediaSource: ClippingMediaSource, recortePlayer: ExoPlayer, player: Player, button1: Any, button2: Any?) {
        loadData(title = title,clippingMediaSource = clippingMediaSource,recortePlayer = recortePlayer, player = player, button1 = button1, button2 = button2)
    }

    private var zoomCallback = object : ZoomSurfaceView.Callback {
        override fun onZoomSurfaceCreated(view: ZoomSurfaceView) {
            recortePlayer?.setVideoSurface(view.surface)
        }

        override fun onZoomSurfaceDestroyed(view: ZoomSurfaceView) {
        }
    }

    fun loadData(title: Any? = null, clippingMediaSource: ClippingMediaSource,recortePlayer : ExoPlayer, player : Player,  button1: Any, completion1: (() -> Unit)? = null, button2: Any? = null, completion2: (() -> Unit)? = null) {
        this.title = title
        this.player = player
        this.recortePlayer = recortePlayer
        this.clippingMediaSource = clippingMediaSource
        this.button1 = button1
        this.button2 = button2
    }

}