package gttlipse;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

public class GTTPerspective implements IPerspectiveFactory 
{
	public GTTPerspective()
	{
		super();
	}
	
	public void createInitialLayout( IPageLayout layout ) 
	{		
		// left
		IFolderLayout left = layout.createFolder( "left", IPageLayout.LEFT, 0.25f, layout.getEditorArea() );	
		left.addView( JavaUI.ID_PACKAGES );
		left.addView( JavaUI.ID_TYPE_HIERARCHY );
		left.addPlaceholder( IPageLayout.ID_PROJECT_EXPLORER );
	
        // bottom
		IFolderLayout bottom = layout.createFolder( "bottom", IPageLayout.BOTTOM, 0.75f, layout.getEditorArea() );
		bottom.addView( IPageLayout.ID_PROBLEM_VIEW );
		bottom.addView( JavaUI.ID_JAVADOC_VIEW );
		bottom.addView( JavaUI.ID_SOURCE_VIEW );
		bottom.addPlaceholder( IPageLayout.ID_BOOKMARKS );
		bottom.addPlaceholder( IProgressConstants.PROGRESS_VIEW_ID );
        bottom.addPlaceholder( NewSearchUI.SEARCH_VIEW_ID );
		bottom.addPlaceholder( IConsoleConstants.ID_CONSOLE_VIEW );
			
		// right
		IFolderLayout right = layout.createFolder( "right", IPageLayout.RIGHT, 0.65f, layout.getEditorArea() );		
		right.addView( "GTTlipse.views.GTTTestScriptView" );
		right.addView( "GTTlipse.FSM.ui.views" );
		right.addView( "GTTlipse.views.macroScriptView" );
		right.addView( IPageLayout.ID_OUTLINE );
		
		// add action	
		layout.addActionSet( IDebugUIConstants.LAUNCH_ACTION_SET );
		layout.addActionSet( JavaUI.ID_ACTION_SET );
		layout.addActionSet( JavaUI.ID_ELEMENT_CREATION_ACTION_SET );
		layout.addActionSet( IPageLayout.ID_NAVIGATE_ACTION_SET );

		// add perspective shortcut
		layout.addPerspectiveShortcut( IDebugUIConstants.ID_DEBUG_PERSPECTIVE );
		layout.addPerspectiveShortcut( JavaUI.ID_PERSPECTIVE );
		layout.addPerspectiveShortcut( JavaUI.ID_BROWSING_PERSPECTIVE );
		
		// add view shortcut
        // views - java
		layout.addShowViewShortcut(JavaUI.ID_PACKAGES);
		layout.addShowViewShortcut(JavaUI.ID_TYPE_HIERARCHY);
		layout.addShowViewShortcut(JavaUI.ID_SOURCE_VIEW );
		layout.addShowViewShortcut(JavaUI.ID_JAVADOC_VIEW );

		// views - search
		layout.addShowViewShortcut( NewSearchUI.SEARCH_VIEW_ID );
		
		// views - debugging
		layout.addShowViewShortcut( IConsoleConstants.ID_CONSOLE_VIEW );

		// views - standard workbench
		layout.addShowViewShortcut( IPageLayout.ID_OUTLINE );
		layout.addShowViewShortcut( IPageLayout.ID_PROBLEM_VIEW );
		layout.addShowViewShortcut( IPageLayout.ID_PROJECT_EXPLORER );
		layout.addShowViewShortcut( "GTTlipse.views.GTTTestScriptView" );
		layout.addShowViewShortcut( "GTTlipse.FSM.ui.views" );
		layout.addShowViewShortcut( "GTTlipse.views.macroScriptView" );
		layout.addShowViewShortcut( "GTTlipse.views.macroScriptView" );
		
		// add new wizard shortcut
		layout.addNewWizardShortcut( "GTTlipse.wizards.GTTNewWizard" );
		layout.addNewWizardShortcut( "org.eclipse.jdt.junit.wizards.NewTestCaseCreationWizard" );
		layout.addNewWizardShortcut( "org.eclipse.jdt.junit.wizards.NewTestSuiteCreationWizard" );
		layout.addNewWizardShortcut( "org.eclipse.jdt.ui.wizards.NewPackageCreationWizard");
		layout.addNewWizardShortcut( "org.eclipse.jdt.ui.wizards.NewClassCreationWizard");
		layout.addNewWizardShortcut( "org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard");
		layout.addNewWizardShortcut( "org.eclipse.jdt.ui.wizards.NewEnumCreationWizard");
		layout.addNewWizardShortcut( "org.eclipse.jdt.ui.wizards.NewAnnotationCreationWizard");
		layout.addNewWizardShortcut( "org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard");
		layout.addNewWizardShortcut( "org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard");
		layout.addNewWizardShortcut( "org.eclipse.ui.wizards.new.folder");
		layout.addNewWizardShortcut( "org.eclipse.ui.wizards.new.file");
		layout.addNewWizardShortcut( "org.eclipse.ui.wizards.new.file");
		layout.addNewWizardShortcut( "org.eclipse.ui.editors.wizards.UntitledTextFileWizard");
	}

}
