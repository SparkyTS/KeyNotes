package NoteTaker;
import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UndoManagerHelper {

    public static Action getUndoAction(UndoManager manager, String label) {
        return new UndoAction(manager, label);
    }

    public static Action getUndoAction(UndoManager manager) {
        return new UndoAction(manager, "Undo");
    }

    public static Action getRedoAction(UndoManager manager, String label) {
        return new RedoAction(manager, label);
    }

    public static Action getRedoAction(UndoManager manager) {
        return new RedoAction(manager, "Redo");
    }

    private static abstract class UndoRedoAction extends AbstractAction {
        UndoManager undoManager = new UndoManager();

        String errorMessage = "Cannot undo";

        String errorTitle = "Undo Problem";

        protected UndoRedoAction(UndoManager manager, String name) {
            super(name);
            undoManager = manager;
        }

        public void setErrorMessage(String newValue) {
            errorMessage = newValue;
        }

        public void setErrorTitle(String newValue) {
            errorTitle = newValue;
        }

        protected void showMessage(Object source) {
            if (source instanceof Component) {
                JOptionPane.showMessageDialog((Component) source, errorMessage,
                        errorTitle, JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.err.println(errorMessage);
            }
        }
    }

    public static class UndoAction extends UndoRedoAction {
        public UndoAction(UndoManager manager, String name) {
            super(manager, name);
            setErrorMessage("Cannot undo");
            setErrorTitle("Undo Problem");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                undoManager.undo();
            } catch (CannotUndoException cannotUndoException) {
                showMessage(actionEvent.getSource());
            }
        }
    }

    public static class RedoAction extends UndoRedoAction {
        String errorMessage = "Cannot redo";

        String errorTitle = "Redo Problem";

        public RedoAction(UndoManager manager, String name) {
            super(manager, name);
            setErrorMessage("Cannot redo");
            setErrorTitle("Redo Problem");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                undoManager.redo();
            } catch (CannotRedoException cannotRedoException) {
                showMessage(actionEvent.getSource());
            }
        }
    }

}
