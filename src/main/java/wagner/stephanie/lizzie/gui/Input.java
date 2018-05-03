package wagner.stephanie.lizzie.gui;

import wagner.stephanie.lizzie.Lizzie;

import java.awt.event.*;

import static java.awt.event.KeyEvent.*;
import wagner.stephanie.lizzie.plugin.PluginManager;

public class Input implements MouseListener, KeyListener, MouseWheelListener, MouseMotionListener {
    private boolean controlIsPressed = false;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        PluginManager.onMousePressed(e);
        int x = e.getX();
        int y = e.getY();

        if (e.getButton() == MouseEvent.BUTTON1) // left mouse click
            Lizzie.frame.onClicked(x, y);
        else if (e.getButton() == MouseEvent.BUTTON3) // right mouse click
            undo();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        PluginManager.onMouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        PluginManager.onMouseMoved(e);
        int x = e.getX();
        int y = e.getY();

        Lizzie.frame.onMouseMoved(x, y);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private void undo() {
        if (Lizzie.frame.isPlayingAgainstLeelaz) {
            Lizzie.frame.isPlayingAgainstLeelaz = false;
        }
        int movesToAdvance = 1;
        if (controlIsPressed)
            movesToAdvance = 10;

        for (int i = 0; i < movesToAdvance; i++)
            Lizzie.board.previousMove();
    }

    private void redo() {
        if (Lizzie.frame.isPlayingAgainstLeelaz) {
            Lizzie.frame.isPlayingAgainstLeelaz = false;
        }
        int movesToAdvance = 1;
        if (controlIsPressed)
            movesToAdvance = 10;

        for (int i = 0; i < movesToAdvance; i++)
            Lizzie.board.nextMove();
    }

    private void nextBranch() {
        if (Lizzie.frame.isPlayingAgainstLeelaz) {
            Lizzie.frame.isPlayingAgainstLeelaz = false;
        }
        Lizzie.board.nextBranch();
    }

    private void previousBranch() {
        if (Lizzie.frame.isPlayingAgainstLeelaz) {
            Lizzie.frame.isPlayingAgainstLeelaz = false;
        }
        Lizzie.board.previousBranch();
    }

    private void moveBranchUp() {
        Lizzie.board.moveBranchUp();
    }

    private void moveBranchDown() {
        Lizzie.board.moveBranchDown();
    }

    private void deleteMove() { Lizzie.board.deleteMove(); }

    @Override
    public void keyPressed(KeyEvent e) {

        PluginManager.onKeyPressed(e);

        int movesToAdvance = 1; // number of moves to advance if control is held down

        switch (e.getKeyCode()) {
            case VK_CONTROL:

                controlIsPressed = true;
                break;

            case VK_META:
                if (System.getProperty("os.name", "").toUpperCase().startsWith("MAC")) {
                    controlIsPressed = true;
                }
                break;

            case VK_RIGHT:
                if (e.isShiftDown()) {
                    moveBranchDown();
                } else {
                    nextBranch();
                }
                break;

            case VK_LEFT:
                if (e.isShiftDown()) {
                    moveBranchUp();
                } else {
                    previousBranch();
                }
                break;

            case VK_UP:
                undo();
                break;

            case VK_PAGE_UP:
                for (int i = 0; i < 10; i++)
                    undo();
                break;

            case VK_DOWN:
                redo();
                break;

            case VK_PAGE_DOWN:
                for (int i = 0; i < 10; i++)
                    redo();
                break;

            case VK_N:
                // stop the ponder
                if (Lizzie.leelaz.isPondering())
                    Lizzie.leelaz.togglePonder();
                LizzieFrame.startNewGame();
                break;
            case VK_SPACE:
                if (Lizzie.frame.isPlayingAgainstLeelaz) {
                    Lizzie.frame.isPlayingAgainstLeelaz = false;
                    Lizzie.leelaz.togglePonder(); // we must toggle twice for it to restart pondering
                }
                Lizzie.leelaz.togglePonder();
                break;

            case VK_P:
                Lizzie.board.pass();
                break;

            case VK_M:
                Lizzie.config.toggleShowMoveNumber();
                break;

            case VK_F:
                Lizzie.config.toggleShowNextMoves();
                break;

            case VK_I:
                // stop the ponder
                if (Lizzie.leelaz.isPondering())
                    Lizzie.leelaz.togglePonder();
                Lizzie.frame.editGameInfo();
                break;
            case VK_S:
                // stop the ponder
                if (Lizzie.leelaz.isPondering())
                    Lizzie.leelaz.togglePonder();
                LizzieFrame.saveSgf();
                break;

            case VK_O:
                if (Lizzie.leelaz.isPondering())
                    Lizzie.leelaz.togglePonder();
                LizzieFrame.openSgf();
                break;

            case VK_V:
                if (controlIsPressed) {
                    Lizzie.frame.pasteSgf();
                } else {
                    Lizzie.config.toggleShowBranch();
                }
                break;

            case VK_HOME:
                while (Lizzie.board.previousMove()) ;
                break;

            case VK_END:
                while (Lizzie.board.nextMove()) ;
                break;

            case VK_X:
                if (!Lizzie.frame.showControls) {
                    if (Lizzie.leelaz.isPondering()) {
                        wasPonderingWhenControlsShown = true;
                        Lizzie.leelaz.togglePonder();
                    } else {
                        wasPonderingWhenControlsShown = false;
                    }
                    Lizzie.frame.drawControls();
                }
                Lizzie.frame.showControls = true;
                Lizzie.frame.repaint();
                break;

            case VK_W:
                Lizzie.config.toggleShowWinrate();
                break;

            case VK_G:
                Lizzie.config.toggleShowVariationGraph();
                break;

            case VK_C:
                if (controlIsPressed) {
                    Lizzie.frame.copySgf();
                } else {
                    Lizzie.frame.toggleCoordinates();
                }
                break;

            case VK_ENTER:
                if (!Lizzie.leelaz.isThinking) {
                    Lizzie.leelaz.sendCommand("time_settings 0 " + Lizzie.config.config.getJSONObject("leelaz").getInt("max-game-thinking-time-seconds") + " 1");
                    Lizzie.frame.playerIsBlack = !Lizzie.board.getData().blackToPlay;
                    Lizzie.frame.isPlayingAgainstLeelaz = true;
                    Lizzie.leelaz.sendCommand("genmove " + (Lizzie.board.getData().blackToPlay ? "B" : "W"));
                }
                break;

            case VK_DELETE:
            case VK_BACK_SPACE:
                deleteMove();
                break;

            default:
        }
        Lizzie.frame.repaint();
    }

    private boolean wasPonderingWhenControlsShown = false;
    @Override
    public void keyReleased(KeyEvent e) {
        PluginManager.onKeyReleased(e);
        switch (e.getKeyCode()) {
            case VK_CONTROL:
                controlIsPressed = false;
                break;

            case VK_META:
                if (System.getProperty("os.name", "").toUpperCase().startsWith("MAC")) {
                    controlIsPressed = false;
                }

            case VK_X:
                if (wasPonderingWhenControlsShown)
                    Lizzie.leelaz.togglePonder();
                Lizzie.frame.showControls = false;
                Lizzie.frame.repaint();
                break;

            default:
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0) {
            redo();
        } else if (e.getWheelRotation() < 0) {
            undo();
        }
    }
}
