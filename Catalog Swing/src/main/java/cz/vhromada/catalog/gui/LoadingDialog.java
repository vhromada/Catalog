package cz.vhromada.catalog.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import cz.vhromada.catalog.commons.CatalogSwingConstant2;
import cz.vhromada.validators.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A class represents dialog for loading.
 *
 * @author Vladimir Hromada
 */
public class LoadingDialog extends JDialog {

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(LoadingDialog.class);

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** Horizontal label size */
	private static final int HORIZONTAL_LABEL_SIZE = 130;

	/** Return status */
	private DialogResult returnStatus = DialogResult.CANCEL;

	/** Application context */
	private ConfigurableApplicationContext context;

	/** Label with time passed. */
	private JLabel progress = new JLabel("0 s");

	/** Creates a new instance of LoadingDialog. */
	public LoadingDialog() {
		super(new JFrame(), "Loading", true);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		progress.setHorizontalAlignment(SwingConstants.CENTER);

		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(createHorizontalLayout(layout));
		layout.setVerticalGroup(createVerticalLayout(layout));

		pack();
		setLocationRelativeTo(getRootPane());

		final LoadingSwingWorker swingWorker = new LoadingSwingWorker();
		swingWorker.execute();
	}

	/**
	 * Returns return status.
	 *
	 * @return return status
	 */
	public DialogResult getReturnStatus() {
		return returnStatus;
	}

	/**
	 * Returns application context.
	 *
	 * @return application context
	 * @throws IllegalStateException if application context hasn't been set
	 */
	public ConfigurableApplicationContext getContext() {
		Validators.validateFieldNotNull(context, "Application context");

		return context;
	}

	/**
	 * Returns horizontal layout of components.
	 *
	 * @param layout layout
	 * @return horizontal layout of components
	 */
	private GroupLayout.SequentialGroup createHorizontalLayout(final GroupLayout layout) {
		return layout.createSequentialGroup().addComponent(progress, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE, HORIZONTAL_LABEL_SIZE);
	}

	/**
	 * Returns vertical layout of components.
	 *
	 * @param layout layout
	 * @return vertical layout of components
	 */
	private GroupLayout.SequentialGroup createVerticalLayout(final GroupLayout layout) {
		return layout.createSequentialGroup().addComponent(progress, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE,
				CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE, CatalogSwingConstant2.VERTICAL_COMPONENT_SIZE);
	}

	/** A class represents swing worker for loading data. */
	private class LoadingSwingWorker extends SwingWorker<ConfigurableApplicationContext, Object> {

		/** Timer */
		private Timer timer;

		/** Passed time */
		private int time;

		@Override
		protected ConfigurableApplicationContext doInBackground() throws Exception {
			timer = new Timer(1000, new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					timerAction();
				}

			});
			timer.start();
			return new ClassPathXmlApplicationContext("applicationContext.xml");
		}

		@Override
		protected void done() {
			try {
				timer.stop();
				context = get();
				returnStatus = DialogResult.OK;
				setVisible(false);
				dispose();
			} catch (final InterruptedException | ExecutionException ex) {
				logger.error("Error in getting data from Swing Worker.", ex);
				System.exit(5);
			}
		}

		@Override
		protected void process(final List<Object> chunks) {
			progress.setText(chunks.get(chunks.size() - 1) + " s");
		}

		/** Performs action for timer. */
		private void timerAction() {
			time++;
			publish(time);
		}

	}

}