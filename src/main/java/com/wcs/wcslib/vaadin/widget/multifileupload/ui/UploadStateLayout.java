package com.wcs.wcslib.vaadin.widget.multifileupload.ui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author gergo
 */
public class UploadStateLayout extends CssLayout {

    private static final String CANCEL_BUTTON_STYLE_CLASS = "multiple-upload-state-cancelbutton";
    private static final String CANCEL_BUTTON_LAYOUT_STYLE_CLASS = "multiple-upload-state-cancelbuttonlayout";
    private static final int POLLING_INTERVAL = 500;
    private final Label fileName = new Label();
    private final Label textualProgress = new Label();
    private final ProgressIndicator pi = new ProgressIndicator();
    private Button cancelButton;
    private VerticalLayout layout;
    private HorizontalLayout cancelLayout;
    private UploadStatePanel uploadStatePanel;
    private FileDetailBean fileDetailBean;

    public UploadStateLayout(final UploadStatePanel uploadStatePanel) {
        this.uploadStatePanel = uploadStatePanel;
        initForm();
    }

    private void initForm() {
        layout = new VerticalLayout();
        addComponent(layout);

        layout.addComponent(fileName);

        pi.setVisible(false);
        pi.setWidth(100, Unit.PERCENTAGE);
        layout.addComponent(pi);

        textualProgress.setVisible(false);

        cancelLayout = new HorizontalLayout();
        cancelLayout.addStyleName(CANCEL_BUTTON_LAYOUT_STYLE_CLASS);
        cancelLayout.setWidth(100, Unit.PERCENTAGE);
        cancelLayout.addComponent(textualProgress);
        cancelButton = new Button();
        cancelLayout.addComponent(cancelButton);
        cancelLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
        layout.addComponent(cancelLayout);
    }

    private Button createNewCancelButton() {
        return FileUploadUtil.createCancelBtn(uploadStatePanel, fileDetailBean, true, false);
    }

    public void setFileName(String fileName) {
        this.fileName.setValue(fileName);
    }

    public void setProgress(long bytesReceived, long contentLength) {
        pi.setValue(new Float(bytesReceived / (float) contentLength));
        textualProgress.setValue(
                FileUploadUtil.getHumanReadableByteCount(bytesReceived, false)
                + " / "
                + FileUploadUtil.getHumanReadableByteCount(contentLength, false));
    }

    public void startStreaming(FileDetailBean fileDetailBean) {
        this.fileDetailBean = fileDetailBean;
        pi.setValue(0f);
        pi.setVisible(true);
        pi.setPollingInterval(POLLING_INTERVAL);
        textualProgress.setVisible(true);
        setFileName(fileDetailBean.getFileName());

        Button newCancelBtn = createNewCancelButton();
        cancelLayout.replaceComponent(cancelButton, newCancelBtn);
        cancelButton = newCancelBtn;
        cancelButton.addStyleName(CANCEL_BUTTON_STYLE_CLASS);
    }

    public FileDetailBean getFileDetailBean() {
        return fileDetailBean;
    }
}