import javax.swing.*;
import javax.swing.text.*;
import java.text.NumberFormat;

class Input extends PlainDocument {
    private JFormattedTextField textField;
    private int limit;

    Input(int limit) {
        super();
        this.limit = limit;
        this.textField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        textField.setDocument(this); // Set document for the text field
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {

        if (str == null) {
            return;
        }

        // Only allow digits
        if (str.matches("\\d+")) {
            if (!str.equals("0") && (getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            } else {
                showWarningDialog("Input tidak boleh 0 atau melebihi batas.");
            }
        } else {
            showWarningDialog("Input harus berupa angka.");
        }
    }

    private void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "WARNING!", JOptionPane.WARNING_MESSAGE);
    }
}