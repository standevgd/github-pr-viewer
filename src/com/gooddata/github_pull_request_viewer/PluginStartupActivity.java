package com.gooddata.github_pull_request_viewer;

import com.gooddata.github_pull_request_viewer.services.FileHighlightService;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;

public class PluginStartupActivity implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        System.out.println("action=plugin_startup status=start");

        final MessageBus messageBus = project.getMessageBus();
        messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorListener());

        System.out.println("action=plugin_startup status=finished");
    }

    private class FileEditorListener implements FileEditorManagerListener {

        @Override
        public void fileOpened(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {
            System.out.println("action=file_editor_file_opened");
        }

        @Override
        public void fileClosed(@NotNull FileEditorManager fileEditorManager, @NotNull VirtualFile virtualFile) {
            System.out.println("action=file_editor_file_closed");
        }

        @Override
        public void selectionChanged(@NotNull FileEditorManagerEvent fileEditorManagerEvent) {
            System.out.println("action=file_editor_selection_changed");

            final FileHighlightService fileHighlightService =
                    ServiceManager.getService(fileEditorManagerEvent.getManager().getProject(),
                            FileHighlightService.class);

            fileHighlightService.highlightFile(fileEditorManagerEvent.getManager());
        }
    }
}
