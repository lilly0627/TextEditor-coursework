package labs.lab9;

public class CurrentAction {
		private String font;
	    private boolean isItalic;
	    private boolean isBold;
	    private String size;
	    private String text;

		public String getFont() {
			return font;
		}
		public void setFace(String face) {
			this.font = face;
		}

		public boolean getItalicCheck() {
			return isItalic;
		}

		public void setItalicCheck(boolean isItalic) {
			this.isItalic = isItalic;
		}

		public boolean isBoldCheck() {
			return isBold;
		}
		public void setBoldCheck(boolean isBold) {
			this.isBold = isBold;
		}
		public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
}