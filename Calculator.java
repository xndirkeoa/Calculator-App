import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Calculator 
{
    int borderwidth = 360;
    int borderhight = 540;

    Color customLightGray = new Color(212, 212, 210);
    // Color customDarkGray =  new Color(80, 80, 80);
    Color customBlack = new Color(28, 28, 28);
    Color customOrange = new Color(255, 149, 0);

    String[] buttonValues = {
        "AC", "+/-", "%", "÷", 
        "7", "8", "9", "×", 
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "√", "="
    };
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};

    JFrame frame = new JFrame("Calculator");
    JLabel displayLable = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();
    
    String A = "0";
    String operator = null;
    String B = null;
    boolean isNextInputNewNumber = false;

    Calculator() {
        frame.setSize(borderwidth, borderhight);
        frame.setLocationRelativeTo(null); /*makes frame middle of the screen */
        frame.setResizable(false); /*use to close application when x press */
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayLable.setBackground(customBlack);
        displayLable.setForeground(Color.white);
        displayLable.setFont(new Font("Arial", Font.PLAIN, 50));
        displayLable.setHorizontalAlignment(JLabel.LEFT);
        displayLable.setText("0");
        displayLable.setOpaque(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLable);
        frame.add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5, 4));

        frame.add(buttonsPanel);

        for (String buttonValue : buttonValues) {
            JButton button = new JButton();
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(customBlack));

            if (Arrays.asList(topSymbols).contains(buttonValue)){
                button.setBackground(customOrange);
                button.setForeground(customBlack);
            }
            else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customOrange);
                button.setForeground(Color.BLACK);
            }
            else {
                button.setBackground(customLightGray);
                button.setForeground(Color.blue);
            }
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton)e.getSource();
                    String buttonValue = button.getText();

                    // Block 1: Right Symbols (+, -, ×, ÷, =)
                    if (Arrays.asList(rightSymbols).contains(buttonValue)) 
                        {   /*Arrays.asList(rightSymbols): 
                            This converts the array into a standard Java List collection.
                            Java arrays do not have a built-in search method. */
                        if (buttonValue.equals("=")) {
                            if (A != null && operator != null) /*buttonValue.equals("="): Confirms the user clicked the equals sign. */
                                {                              /*A != null: Ensures the first number has been stored.operator != null: Ensures a mathematical operation (like +, -, *, or /) has been selected. */
                                String currentText = displayLable.getText();
                                int operatorIndex = currentText.indexOf(operator);
                                if(operatorIndex == -1) return;
                                B = currentText.substring(operatorIndex + 1).trim();
                                if(B.isEmpty()) return;
                                double numA = Double.parseDouble(A);
                                double numB = Double.parseDouble(B);
                                double result = 0;
                                switch (operator) {
                                    case "+": result = numA + numB; break;
                                    case "-": result = numA - numB; break;
                                    case "×": result = numA * numB; break;
                                    case "÷": 
                                        if(numB != 0){
                                            result = numA/numB;
                                            }else {
                                                displayLable.setText("Error");
                                                clearAll();
                                                return;
                                            }
                                            break;
                                        }
                                        String finalResult = removeZeroDecimal(result);
                                        displayLable.setText (A + " " + operator + " " + B + " = " + finalResult);
                                        A = finalResult;
                                        Calculator.this.operator = null;
                                        B = null;
                                        isNextInputNewNumber = true;
                            }

                        }
                        else 
                        // Setting operation modes (+, -, ×, ÷)
                        {
                            if(operator != null && !isNextInputNewNumber){
                                operator = buttonValue;
                                displayLable.setText(A + " " + operator + " ");
                            }else{
                            A = displayLable.getText();
                            operator = buttonValue;
                            displayLable.setText(A + " " + operator + " ");
                            isNextInputNewNumber = false;
                        }  
                    }
                }
                    // Block 2: Top Symbols (AC, +/-, %)
                    else if (Arrays.asList(topSymbols).contains(buttonValue)) 
                        {
                        if(buttonValue.equals("AC")) {
                            clearAll();
                            displayLable.setText("0");
                        }
                        else if (buttonValue.equals("+/-")) {
                            double numDisplay = Double.parseDouble(displayLable.getText());
                            numDisplay *= -1;
                            displayLable.setText(removeZeroDecimal(numDisplay));
                        }
                        else if (buttonValue.equals("%")) {
                            double numDisplay = Double.parseDouble(displayLable.getText());
                            numDisplay /= 100;
                            displayLable.setText(removeZeroDecimal(numDisplay));
                        }
                    }
                    // Block 3: Numbers, Decimals, and Square Roots
                    else {
                        if (isNextInputNewNumber) {
                                clearAll(); 
                                displayLable.setText(""); 
                            }
                        if (buttonValue.equals("√")) {  // {Separated square root logic cleanly}
                            double numDisplay = Double.parseDouble(displayLable.getText());
                            if (numDisplay >= 0) {
                                displayLable.setText(removeZeroDecimal(Math.sqrt(numDisplay)));
                            } else {
                                displayLable.setText("Error");
                            }   
                        }
                        else if (buttonValue.equals(".")) {
                            if (!displayLable.getText().contains(".")) {
                                displayLable.setText(displayLable.getText() + buttonValue);
                            }
                        }
                        else if ("0123456789".contains(buttonValue)) {
                            if (displayLable.getText().equals("0")) {
                                displayLable.setText(buttonValue);
                            } else {
                                displayLable.setText(displayLable.getText() + buttonValue);
                            }
                        }
                    }
                }
            });
        }
        frame.setVisible(true);
    }
    void clearAll() {
    A = "0";
    Calculator.this.operator = null; 
    B = null;
    isNextInputNewNumber = false;
    }

    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int)numDisplay);
        }
        return Double.toString(numDisplay);
    }
}