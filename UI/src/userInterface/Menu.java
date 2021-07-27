package userInterface;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu
{
    //Members
    private final String[] m_Options;
    private final String m_Title;
    private final StringBuilder m_Menu;
    private final int m_SizeOfOption;

    //Constructors
    public Menu(final String title, final String[] options)
    {
        m_Menu = new StringBuilder();
        m_Options = options;
        m_Title = title;
        m_SizeOfOption = m_Options.length;
        CreateMenu();
    }

    //Methods
    private void CreateMenu() {
        int countOption = 1;
        m_Menu.append(m_Title);
        m_Menu.append( System.lineSeparator());
        for (String option: m_Options) {
            m_Menu.append(countOption);
            m_Menu.append(". ");
            m_Menu.append(option);
            m_Menu.append( System.lineSeparator());
            countOption++;
        }
        m_Menu.append("Please enter one option from the menu(Integer between 1-" + m_SizeOfOption + ")");
        m_Menu.append(": ");

    }

    public void Draw()
    {
        System.out.println(m_Menu);
    }

    public int getInputFromMenu()
    {
        int input = 0;
        boolean isInputValid = false;
        Scanner scanner = new Scanner(System.in);
        while(!isInputValid)
        {
            try
            {
                input = scanner.nextInt();
                if(input < 1 || input > m_SizeOfOption)
                {
                    System.out.println("The input is not between 1-" + m_SizeOfOption + ". Please try again:");
                }
                else
                {
                    isInputValid = true;
                }
            }
            catch(InputMismatchException exception)
            {
                System.out.println("The input is not an Integer. Please try again:");
                scanner.nextLine();
            }
        }
        return input;
    }
}
