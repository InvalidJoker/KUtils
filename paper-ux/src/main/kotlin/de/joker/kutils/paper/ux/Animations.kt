package de.joker.kutils.paper.ux

import de.joker.kutils.paper.PluginInstance
import de.joker.kutils.paper.extensions.interpolateColorText
import de.joker.kutils.paper.extensions.minecraftTicks
import dev.fruxz.stacked.extension.Times
import dev.fruxz.stacked.extension.Title
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.entity.Entity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toKotlinDuration

/**
 * This function shows the animated title to [this] given [Entity],
 * @param title the title-frames to show
 * @param subtitle the subtitle-frames to show
 * @param times the times of the title
 * @param timeBetweenTicks the duration between every frame
 * @param alignment the alignment of the shorter animation-frames (if title or subtitle animation is shorter than the other one)
 * @param renderFadeIn if the first frame should have the fadeIn-effect of [times]
 * @return the running job
 * @author Fruxz
 * @since 1.0
 */
fun Entity.showAnimatedTitle(title: List<Component>, subtitle: List<Component>, times: Times = Times(20.minecraftTicks, 60.minecraftTicks, 20.minecraftTicks), timeBetweenTicks: Duration = .1.seconds, alignment: KeyFramesAlignment = KeyFramesAlignment.CENTER, renderFadeIn: Boolean = true) = PluginInstance.coroutineScope.launch {
    if (title.isEmpty() || subtitle.isEmpty()) throw IllegalArgumentException("Title and subtitle must not be empty")

    val (titles, subtitles) = title.toMutableList() to subtitle.toMutableList()

    fun alignToSize(target: MutableList<Component>, size: Int) {
        while (target.size < size) {
            when (alignment) {
                KeyFramesAlignment.START -> target.add(target.first())
                KeyFramesAlignment.END -> target.add(0, target.last())
                KeyFramesAlignment.CENTER -> {
                    if (target.size % 2 == 0) {
                        target.add(0, target.first())
                    } else
                        target.add(target.last())
                }
            }
        }
    }

    if (titles.size > subtitles.size) alignToSize(subtitles, titles.size) else if (titles.size < subtitles.size) alignToSize(titles, subtitles.size)

    if (renderFadeIn) {
        this@showAnimatedTitle.showTitle(Title(titles.first(), subtitles.first(), times))
        delay(times.fadeIn().toKotlinDuration())
        titles.drop(1)
        subtitles.drop(1)
    }

    titles.withIndex().forEach { (index, title) ->
        subtitles[index].let { subtitle ->
            this@showAnimatedTitle.showTitle(Title(title, subtitle, Times(Duration.ZERO, times.stay().toKotlinDuration(), times.fadeOut().toKotlinDuration())))
        }
        delay(timeBetweenTicks)
    }

}

/**
 * This function shows the animated title to [this] given [Entity],
 * @param title the title-frames to show
 * @param subtitle the single subtitle-frame to show
 * @param times the times of the title
 * @param timeBetweenTicks the duration between every frame
 * @param alignment the alignment of the shorter animation-frames (if title or subtitle animation is shorter than the other one)
 * @param renderFadeIn if the first frame should have the fadeIn-effect of [times]
 * @return the running job
 * @author Fruxz
 * @since 1.0
 */
fun Entity.showAnimatedTitle(title: List<Component>, subtitle: Component, times: Times = Times(20.minecraftTicks, 60.minecraftTicks, 20.minecraftTicks), timeBetweenTicks: Duration = .15.seconds, alignment: KeyFramesAlignment = KeyFramesAlignment.CENTER, renderFadeIn: Boolean = true) =
    showAnimatedTitle(title, listOf(subtitle), times, timeBetweenTicks, alignment, renderFadeIn)

/**
 * This function shows the animated title to [this] given [Entity],
 * @param title the single title-frame to show
 * @param subtitle the subtitle-frames to show
 * @param times the times of the title
 * @param timeBetweenTicks the duration between every frame
 * @param alignment the alignment of the shorter animation-frames (if title or subtitle animation is shorter than the other one)
 * @param renderFadeIn if the first frame should have the fadeIn-effect of [times]
 * @return the running job
 * @author Fruxz
 * @since 1.0
 */
fun Entity.showAnimatedTitle(title: Component, subtitle: List<Component>, times: Times = Times(20.minecraftTicks, 60.minecraftTicks, 20.minecraftTicks), timeBetweenTicks: Duration = .15.seconds, alignment: KeyFramesAlignment = KeyFramesAlignment.CENTER, renderFadeIn: Boolean = true) =
    showAnimatedTitle(listOf(title), subtitle, times, timeBetweenTicks, alignment, renderFadeIn)

/**
 * This enum class helps to define, in which timing
 * the 2 different animations of the [showAnimatedTitle]
 * function should be aligned.
 * @author Fruxz
 * @since 1.0
 */
enum class KeyFramesAlignment {
    START,
    CENTER,
    END;
}

enum class TitlePosition {
    TITLE,
    SUBTITLE;
}

fun Entity.showColorAnimatedTitle(
    title: String,
    position: TitlePosition = TitlePosition.TITLE,
    startColor: TextColor = TextColor.color(255, 255, 255), // white
    endColor: TextColor = TextColor.color(255, 165, 0), // orange
    times: Times = Times(20.minecraftTicks, 60.minecraftTicks, 20.minecraftTicks),
    timeBetweenTicks: Duration = .15.seconds,
    alignment: KeyFramesAlignment = KeyFramesAlignment.CENTER,
    renderFadeIn: Boolean = true
) = {
    val animatedTitle = interpolateColorText(
        text = title,
        steps = 20,
        startColor = startColor,
        endColor = endColor
    )

    when (position) {
        TitlePosition.TITLE -> showAnimatedTitle(animatedTitle, emptyList(), times, timeBetweenTicks, alignment, renderFadeIn)
        TitlePosition.SUBTITLE -> showAnimatedTitle(emptyList(), animatedTitle, times, timeBetweenTicks, alignment, renderFadeIn)
    }
}