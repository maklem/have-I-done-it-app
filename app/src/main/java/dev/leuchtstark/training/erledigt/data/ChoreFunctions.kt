package dev.leuchtstark.training.erledigt.data

import androidx.annotation.VisibleForTesting
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

private const val GRACE_PERIOD = 2*3600L

fun ChoreInformation.nextTimeDue(): Long
{
    return this.nextTimeDueForInstant(Instant.now())
}

fun ChoreInformation.approximateNextResetTime(): Long
{
    return this.nextTimeDueForInstant(Instant.now()) - GRACE_PERIOD
}

@VisibleForTesting
private fun ChoreInformation.nextTimeDueForInstant(now: Instant): Long
{
    val reminderTime = LocalTime.ofSecondOfDay(remindAtSecondOfDay)
    val localZone = ZoneOffset.systemDefault()

    var next = LocalDateTime.of(now.atZone(localZone).toLocalDate(), reminderTime)
    var thenOffset = localZone.rules.getOffset(next)

    if(next.toEpochSecond(thenOffset) <= now.epochSecond)
    {
        next = next.plusDays(1)
        thenOffset = localZone.rules.getOffset(next)
    }
    if(lastTimeDone != null && lastTimeDone > next.toEpochSecond(thenOffset) - GRACE_PERIOD)
    {
        next = next.plusDays(1)
        thenOffset = localZone.rules.getOffset(next)
    }
    return next.toEpochSecond(thenOffset)
}

fun ChoreInformation.isDue(): Boolean
{
    return this.isDueAtInstant(Instant.now())
}

@VisibleForTesting
private fun ChoreInformation.isDueAtInstant(now: Instant): Boolean
{
    if(lastTimeDone == null)
    {
        return true
    }

    val reminderTime = LocalTime.ofSecondOfDay(remindAtSecondOfDay)
    val localZone = ZoneOffset.systemDefault()

    var currentEvent = LocalDateTime.of(now.atZone(localZone).toLocalDate(), reminderTime)
    var thenOffset = localZone.rules.getOffset(currentEvent)

    if(now.epochSecond < currentEvent.toEpochSecond(thenOffset) - GRACE_PERIOD)
    {
        currentEvent = currentEvent.minusDays(1)
        thenOffset = localZone.rules.getOffset(currentEvent)
    }
    return lastTimeDone < currentEvent.toEpochSecond(thenOffset) - GRACE_PERIOD
}