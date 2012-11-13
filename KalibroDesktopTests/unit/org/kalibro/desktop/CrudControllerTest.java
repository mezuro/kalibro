package org.kalibro.desktop;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ReadingGroup;
import org.kalibro.core.reflection.MethodReflector;
import org.kalibro.desktop.swingextension.dialog.ChoiceDialog;
import org.kalibro.desktop.swingextension.panel.EditPanel;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.*")
@PrepareOnlyThisForTest({CrudController.class, KalibroFrame.class})
public class CrudControllerTest extends UnitTest {

	private MethodReflector reflector;
	private EditPanel<ReadingGroup> panel;
	private KalibroFrame frame;
	private ReadingGroup group;

	private CrudController<ReadingGroup> controller;

	@Before
	public void setUp() throws Exception {
		panel = mock(EditPanel.class);
		group = loadFixture("scholar", ReadingGroup.class);
		mockReflector();
		mockKalibroFrame();
		controller = new GroupController();
	}

	private void mockReflector() throws Exception {
		reflector = mock(MethodReflector.class);
		whenNew(MethodReflector.class).withArguments(ReadingGroup.class).thenReturn(reflector);
		when(reflector.invoke("all")).thenReturn(set(group));
	}

	private void mockKalibroFrame() {
		frame = mock(KalibroFrame.class);
		mockStatic(KalibroFrame.class);
		when(KalibroFrame.getInstance()).thenReturn(frame);
	}

	@Test
	public void shouldGetClassName() {
		assertEquals("Reading group", controller.getClassName().asText());
		assertEquals("readingGroup", controller.getClassName().asVariable());
	}

	@Test
	public void shouldCreate() throws Exception {
		controller.create();
		verify(frame).addTab("New group - Reading group", panel);
	}

	@Test
	public void shouldOpen() throws Exception {
		ChoiceDialog<ReadingGroup> dialog = mockChooseDialog("Open", group);
		controller.open();

		InOrder order = Mockito.inOrder(dialog, frame);
		order.verify(dialog).choose("Select reading group:", set(group));
		order.verify(frame).addTab("Scholar - Reading group", panel);
	}

	@Test
	public void shouldNotOpenIfUserCancels() throws Exception {
		ChoiceDialog<ReadingGroup> dialog = mockChooseDialog("Open", null);
		controller.open();

		verify(dialog).choose("Select reading group:", set(group));
		verify(frame, never()).addTab(anyString(), any(EditPanel.class));
	}

	@Test
	public void shouldDelete() throws Exception {
		ChoiceDialog<ReadingGroup> dialog = mockChooseDialog("Delete", group);
		controller.delete();

		InOrder order = Mockito.inOrder(dialog, reflector);
		order.verify(dialog).choose("Select reading group:", set(group));
		order.verify(reflector).invoke(group, "delete");
	}

	@Test
	public void shouldNotDeleteIfUserCancels() throws Exception {
		ChoiceDialog<ReadingGroup> dialog = mockChooseDialog("Delete", null);
		controller.delete();

		verify(dialog).choose("Select reading group:", set(group));
		verify(reflector, never()).invoke(any(), anyString());
	}

	private ChoiceDialog<ReadingGroup> mockChooseDialog(String title, ReadingGroup choice) throws Exception {
		ChoiceDialog<ReadingGroup> dialog = mock(ChoiceDialog.class);
		whenNew(ChoiceDialog.class).withArguments(null, title).thenReturn(dialog);
		when(dialog.getChoice()).thenReturn(choice);
		return dialog;
	}

	@Test
	public void shouldSave() {
		save();
		verify(reflector).invoke(group, "save");
	}

	@Test
	public void shouldUpdateTitleOnSave() {
		save();
		verify(frame).setSelectedTitle("Scholar - Reading group");
	}

	private void save() {
		doReturn(panel).when(frame).getSelectedTab();
		when(panel.get()).thenReturn(group);
		controller.save();
	}

	@Test
	public void shouldClose() {
		controller.close();
		verify(frame).removeSelectedTab();
	}

	private class GroupController extends CrudController<ReadingGroup> {

		@Override
		protected Class<ReadingGroup> entityClass() {
			return ReadingGroup.class;
		}

		@Override
		protected EditPanel<ReadingGroup> panelFor(ReadingGroup entity) {
			return panel;
		}
	}
}