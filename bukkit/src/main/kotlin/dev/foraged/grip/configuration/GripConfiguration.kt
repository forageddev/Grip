package dev.foraged.grip.configuration

import xyz.mkotb.configapi.comment.Comment
import xyz.mkotb.configapi.comment.HeaderComment

@HeaderComment("Hai this is config file for grip shit lmao")
class GripConfiguration
{
    val groups: List<String> = mutableListOf()
}