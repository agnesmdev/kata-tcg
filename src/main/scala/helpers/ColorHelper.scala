package helpers

object ColorHelper {

  implicit class ColorUtil(text: String) {
    def firstPlayer: String = s"${Console.YELLOW}${Console.BOLD}$text${Console.RESET}"

    def secondPlayer: String = s"${Console.CYAN}${Console.BOLD}$text${Console.RESET}"

    def info: String = s"${Console.BLUE}${Console.BOLD}$text${Console.RESET}"

    def heal: String = s"${Console.GREEN}${Console.BOLD}$text${Console.RESET}"

    def damage: String = s"${Console.RED}${Console.BOLD}$text${Console.RESET}"
  }

}
