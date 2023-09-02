package game.enemies;

/**
 * Use this enum class to give capabilities for Enemy.
 * Created by:
 * @author Bryan Wong
 * Modified by:
 * @author Po Han Tay
 * @version 1.0
 */

public enum EnemyStatus {
    FOLLOWING,
    RESURRECTABLE,
    CAN_AREA_ATTACK,
    CAN_DROP_RUNES,
    HOSTILE_TO_ENEMY,
    HOSTILE_TO_PLAYER,
    NOT_HOSTILE_TO_UNDEAD,
    NOT_HOSTILE_TO_ANIMAL,
    NOT_HOSTILE_TO_MARINE,
    NOT_HOSTILE_TO_STORMVEIL_ENEMY,
    NOT_HOSTILE_TO_ALLY,
    NOT_HOSTILE_TO_INVADER,
    DEMIGOD,
    FIRST_PHASE,
    SUMMONABLE,
}
