package de.joker.kutils.paper.luckperms

import net.luckperms.api.model.group.Group
import net.luckperms.api.node.NodeType
import java.util.UUID

val UUID.groups: List<Group>?
    get() = luckPerms.userManager.getUser(this)?.nodes?.filter { NodeType.INHERITANCE.matches(it) }
        ?.mapNotNull { NodeType.INHERITANCE.cast(it).groupName }
        ?.mapNotNull { luckPerms.groupManager.getGroup(it) }

val UUID.groupsWithExpiry: List<Pair<Group, Long?>>?
    get() = luckPerms.userManager.getUser(this)?.nodes?.filter { NodeType.INHERITANCE.matches(it) }
        ?.mapNotNull { NodeType.INHERITANCE.cast(it) }?.map { luckPerms.groupManager.getGroup(it.groupName)!! to it.expiry?.toEpochMilli() }

val UUID.primaryGroup: Group?
    get() = groups?.maxByOrNull { it.absoluteWeight }