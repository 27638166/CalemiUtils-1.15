package calemiutils.gui.base;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class TextFieldRect extends TextFieldWidget {

    public TextFieldRect (FontRenderer fontRenderer, int x, int y, int width, int stringLimit, String defaultString) {

        super(fontRenderer, x + 1, y + 2, width, 12, defaultString);
        setTextColor(-1);
        setDisabledTextColour(-1);
        setEnableBackgroundDrawing(true);
        setMaxStringLength(stringLimit);
        setText(defaultString);
        setCanLoseFocus(true);
    }
}
