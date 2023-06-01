package com.toools.videocrop

import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ClippingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import es.dmoral.toasty.Toasty
import java.util.concurrent.TimeUnit

class VideoCropHelper {

    var imgDefault : Int? = null

    var backgroundColor : Int? = null
    var iconThinColor : Int? = null
    var iconBackgroundColor : Int? = null
    var textColor : Int? = null
    var buttonColor: Int? = null
    var buttonTextColor: Int? = null

    var titleFont: Typeface? = null
    var descriptionFont: Typeface? = null
    var buttonFont: Typeface? = null


    fun initDefaultValues(imgDefault : Int, backgroundColor : Int, iconThinColor : Int?, iconBackgroundColor : Int, textColor : Int, buttonColor : Int,
                          titleFont: Typeface? = null, descriptionFont: Typeface? = null, buttonFont: Typeface? = null, buttonTextColor : Int? = null){
        this.imgDefault = imgDefault
        this.backgroundColor = backgroundColor
        this.iconThinColor = iconThinColor
        this.iconBackgroundColor = iconBackgroundColor
        this.textColor = textColor
        this.buttonColor = buttonColor

        this.titleFont = titleFont
        this.descriptionFont = descriptionFont
        this.buttonFont = buttonFont
        this.buttonTextColor = buttonTextColor

    }

    //view = binding.root
    fun recorte(activity: AppCompatActivity, player: Player, view : View){

        player.pause()

        val dataSourceFactory = DefaultDataSource.Factory(view.context)
        lateinit var mediaSource : MediaSource
        val cropPosition : Long = player.currentPosition + 10000L

        showThreeButtonsAlert(
            activity = activity,
            title = R.string.recorte,
            text = activity.getString(R.string.recorte_desc, formatPosition(player.currentPosition),formatPosition(cropPosition)),
            icon = imgDefault,
            button1 = R.string.recorte_guardar,
            completion1 = {
                guardarVideo()
            },
            button2 = R.string.recorte_ver,
            completion2 = {
                player.pause()

                player.currentMediaItem?.let{
                    mediaSource =  HlsMediaSource.Factory(dataSourceFactory).createMediaSource(it)
                }?:run{
                    Toasty.warning(activity,R.string.recorte_error, Toast.LENGTH_SHORT,true).show()
                }

                val clippingSource = ClippingMediaSource(mediaSource, player.currentPosition * 1000, cropPosition * 1000)
                val recortePlayer = ExoPlayer.Builder(view.context).build()

                showVideoAlert(
                    activity = activity,
                    title = R.string.recorte_titulo,
                    clippingMediaSource = clippingSource,
                    player = player,
                    recortePlayer = recortePlayer,
                    button1 = R.string.recorte_guardar,
                    button2 = R.string.recorte_cancelar
                )
            },
            button3 = R.string.recorte_cancelar,
            completion3 = {
                player.play()
            }
        )
    }

    fun relaeseRecortePlayer(){
        DialogVideoFragment.getInstance().releasePlayer()
    }

    private fun formatPosition(position : Long) : String {
        // Convertir los milisegundos a minutos y segundos
        val minutes = TimeUnit.MILLISECONDS.toMinutes(position)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(position) - TimeUnit.MINUTES.toSeconds(minutes)

        // Formatear los minutos y segundos como una cadena
        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        return formattedTime
    }

    fun showThreeButtonsAlert(activity: AppCompatActivity, title: Any? = null, text: Any, icon: Int? = imgDefault, button1: Any, completion1: (() -> Unit)? = null, button2: Any?, completion2: (() -> Unit)? = null, button3: Any?, completion3: (() -> Unit)? = null) {
        val dialog = DialogThreeButtonsFragment(imgDefault, backgroundColor, iconThinColor, iconBackgroundColor, textColor, buttonColor, titleFont, descriptionFont, buttonFont, buttonTextColor)
        dialog.showThreeButtons(title = title, text = text, icon = icon, button1 = button1, completion1 = completion1, button2 = button2, completion2 = completion2, button3 = button3, completion3 = completion3)
        dialog.show(activity.supportFragmentManager, "threeButtonsAlert")
    }

    fun showVideoAlert(activity: AppCompatActivity, title: Any? = null, clippingMediaSource: ClippingMediaSource, recortePlayer: ExoPlayer, player: Player, button1: Any, button2: Any?) {
        val dialog = DialogVideoFragment(backgroundColor, textColor, buttonColor, titleFont, buttonFont, buttonTextColor)
        dialog.showVideo(title = title,clippingMediaSource = clippingMediaSource,recortePlayer = recortePlayer, player = player, button1 = button1, button2 = button2)
        dialog.show(activity.supportFragmentManager, "VideoAlert")
    }

    companion object {

        private var mInstance = VideoCropHelper()
        @Synchronized
        fun getInstance(): VideoCropHelper {
            return mInstance
        }
        fun guardarVideo(){}
    }
}