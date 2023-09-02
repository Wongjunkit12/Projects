package game.displays;

/**
 * Fancy messages used to print the game title
 * Font obtained from: https://patorjk.com/software/taag/#p=display&f=Georgia11&t=
 * Font: Georgia11
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 *
 */

public class FancyMessage {
    /**
     * FancyMessage displayed at the start of the game.
     */
    public static final String ELDEN_RING =
            "`7MM\"\"\"YMM  `7MMF'      `7MM\"\"\"Yb. `7MM\"\"\"YMM  `7MN.   `7MF'    `7MM\"\"\"Mq.  `7MMF'`7MN.   `7MF' .g8\"\"\"bgd  \n" +
            "  MM    `7    MM          MM    `Yb. MM    `7    MMN.    M        MM   `MM.   MM    MMN.    M .dP'     `M  \n" +
            "  MM   d      MM          MM     `Mb MM   d      M YMb   M        MM   ,M9    MM    M YMb   M dM'       `  \n" +
            "  MMmmMM      MM          MM      MM MMmmMM      M  `MN. M        MMmmdM9     MM    M  `MN. M MM           \n" +
            "  MM   Y  ,   MM      ,   MM     ,MP MM   Y  ,   M   `MM.M        MM  YM.     MM    M   `MM.M MM.    `7MMF'\n" +
            "  MM     ,M   MM     ,M   MM    ,dP' MM     ,M   M     YMM        MM   `Mb.   MM    M     YMM `Mb.     MM  \n" +
            ".JMMmmmmMMM .JMMmmmmMMM .JMMmmmdP' .JMMmmmmMMM .JML.    YM      .JMML. .JMM..JMML..JML.    YM   `\"bmmmdPY  \n";

    /**
     * FancyMessage displayed when Tarnished dies.
     */
    public static final String YOU_DIED =
            "`YMM'   `MM' .g8\"\"8q. `7MMF'   `7MF'    `7MM\"\"\"Yb. `7MMF'`7MM\"\"\"YMM  `7MM\"\"\"Yb.   \n" +
            "  VMA   ,V .dP'    `YM. MM       M        MM    `Yb. MM    MM    `7    MM    `Yb. \n" +
            "   VMA ,V  dM'      `MM MM       M        MM     `Mb MM    MM   d      MM     `Mb \n" +
            "    VMMP   MM        MM MM       M        MM      MM MM    MMmmMM      MM      MM \n" +
            "     MM    MM.      ,MP MM       M        MM     ,MP MM    MM   Y  ,   MM     ,MP \n" +
            "     MM    `Mb.    ,dP' YM.     ,M        MM    ,dP' MM    MM     ,M   MM    ,dP' \n" +
            "   .JMML.    `\"bmmd\"'    `bmmmmd\"'      .JMMmmmdP' .JMML..JMMmmmmMMM .JMMmmmdP'   \n";

    /**
     * FancyMessage displayed when a new Site of Lost Grace is activated.
     */
    public static final String LOST_GRACE_DISCOVERED =
            "\n" +
            "                                                                                                                                                                                                                                     \n" +
            "                                                                                                                                                                                                                                     \n" +
            "`7MMF'        .g8\"\"8q.    .M\"\"\"bgd MMP\"\"MM\"\"YMM       .g8\"\"\"bgd `7MM\"\"\"Mq.        db       .g8\"\"\"bgd `7MM\"\"\"YMM      `7MM\"\"\"Yb. `7MMF' .M\"\"\"bgd   .g8\"\"\"bgd   .g8\"\"8q.`7MMF'   `7MF'`7MM\"\"\"YMM  `7MM\"\"\"Mq.  `7MM\"\"\"YMM  `7MM\"\"\"Yb.   \n" +
            "  MM        .dP'    `YM. ,MI    \"Y P'   MM   `7     .dP'     `M   MM   `MM.      ;MM:    .dP'     `M   MM    `7        MM    `Yb. MM  ,MI    \"Y .dP'     `M .dP'    `YM.`MA     ,V    MM    `7    MM   `MM.   MM    `7    MM    `Yb. \n" +
            "  MM        dM'      `MM `MMb.          MM          dM'       `   MM   ,M9      ,V^MM.   dM'       `   MM   d          MM     `Mb MM  `MMb.     dM'       ` dM'      `MM VM:   ,V     MM   d      MM   ,M9    MM   d      MM     `Mb \n" +
            "  MM        MM        MM   `YMMNq.      MM          MM            MMmmdM9      ,M  `MM   MM            MMmmMM          MM      MM MM    `YMMNq. MM          MM        MM  MM.  M'     MMmmMM      MMmmdM9     MMmmMM      MM      MM \n" +
            "  MM      , MM.      ,MP .     `MM      MM          MM.    `7MMF' MM  YM.      AbmmmqMA  MM.           MM   Y  ,       MM     ,MP MM  .     `MM MM.         MM.      ,MP  `MM A'      MM   Y  ,   MM  YM.     MM   Y  ,   MM     ,MP \n" +
            "  MM     ,M `Mb.    ,dP' Mb     dM      MM          `Mb.     MM   MM   `Mb.   A'     VML `Mb.     ,'   MM     ,M       MM    ,dP' MM  Mb     dM `Mb.     ,' `Mb.    ,dP'   :MM;       MM     ,M   MM   `Mb.   MM     ,M   MM    ,dP' \n" +
            ".JMMmmmmMMM   `\"bmmd\"'   P\"Ybmmd\"     .JMML.          `\"bmmmdPY .JMML. .JMM..AMA.   .AMMA. `\"bmmmd'  .JMMmmmmMMM     .JMMmmmdP' .JMML.P\"Ybmmd\"    `\"bmmmd'    `\"bmmd\"'      VF      .JMMmmmmMMM .JMML. .JMM..JMMmmmmMMM .JMMmmmdP'   \n" +
            "                                                                                                                                                                                                                                     \n" +
            "                                                                                                                                                                                                                                     \n" +
            "\n";

    /**
     * FancyMessage displayed when a Demigod is slain.
     */
    public static final String DEMIGOD_FELLED =
            "\n" +
            "                                                                                                                                                             \n" +
            "                                                                                                                                                             \n" +
            "`7MM\"\"\"Yb. `7MM\"\"\"YMM  `7MMM.     ,MMF'`7MMF' .g8\"\"\"bgd    .g8\"\"8q. `7MM\"\"\"Yb.       `7MM\"\"\"YMM `7MM\"\"\"YMM  `7MMF'      `7MMF'      `7MM\"\"\"YMM  `7MM\"\"\"Yb.   \n" +
            "  MM    `Yb. MM    `7    MMMb    dPMM    MM .dP'     `M  .dP'    `YM. MM    `Yb.       MM    `7   MM    `7    MM          MM          MM    `7    MM    `Yb. \n" +
            "  MM     `Mb MM   d      M YM   ,M MM    MM dM'       `  dM'      `MM MM     `Mb       MM   d     MM   d      MM          MM          MM   d      MM     `Mb \n" +
            "  MM      MM MMmmMM      M  Mb  M' MM    MM MM           MM        MM MM      MM       MM\"\"MM     MMmmMM      MM          MM          MMmmMM      MM      MM \n" +
            "  MM     ,MP MM   Y  ,   M  YM.P'  MM    MM MM.    `7MMF'MM.      ,MP MM     ,MP       MM   Y     MM   Y  ,   MM      ,   MM      ,   MM   Y  ,   MM     ,MP \n" +
            "  MM    ,dP' MM     ,M   M  `YM'   MM    MM `Mb.     MM  `Mb.    ,dP' MM    ,dP'       MM         MM     ,M   MM     ,M   MM     ,M   MM     ,M   MM    ,dP' \n" +
            ".JMMmmmdP' .JMMmmmmMMM .JML. `'  .JMML..JMML. `\"bmmmdPY    `\"bmmd\"' .JMMmmmdP'       .JMML.     .JMMmmmmMMM .JMMmmmmMMM .JMMmmmmMMM .JMMmmmmMMM .JMMmmmdP'   \n" +
            "                                                                                                                                                             \n" +
            "                                                                                                                                                             \n" +
            "\n";
}
