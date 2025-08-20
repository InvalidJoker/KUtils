package de.joker.kutils.core.module

import de.joker.kutils.core.annotation.KUtilsInternal

/**
 * The Entrypoint to Platform modules
 *
 * Every Platform defines their Module Class (paper: PaperModule, fabric: FabricModule, ...) with their given entrypoint.
 *
 * If a module is imported, its Entrypoint (the class implementing the respective Module class)
 * is added to the module list in the Platforms entrypoint (Paper: the KPlugin class).
 * The load, start and stop functions will then be called at the right times.
 *
 * - [load] is called at the very beginning of the startup sequence (paper: when the server first loads and the worlds
 * for example aren't loaded yet). This should be used scarcely and most things should be done in [start]
 * - [start] is called when everything is ready for the module to start. This should be the main function used
 * - [stop] is called when the platform is being shut down.
 *
 * @author Max Bossing
 * @since 0.0.1
 */
@KUtilsInternal
interface ModuleBase<T> {
    fun load(entrypoint: T)
    fun start(entrypoint: T)
    fun stop(entrypoint: T)
}