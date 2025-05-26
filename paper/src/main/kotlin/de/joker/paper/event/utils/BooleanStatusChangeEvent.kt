package de.joker.paper.event.utils

abstract class BooleanStatusChangeEvent(var newValue: Boolean, isAsync: Boolean = false) : KEvent(isAsync)