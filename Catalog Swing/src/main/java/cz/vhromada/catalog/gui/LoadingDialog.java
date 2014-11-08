package cz.vhromada.catalog.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

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

	/** Creates a new instance of LoadingDialog. */
	public LoadingDialog() {
		super(new JFrame(), "Loading", true);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);

		final JProgressBar progressBar = new JProgressBar(0, 5);
		progressBar.setStringPainted(true);
		final PropertyChangeListener progressListener = new PropertyChangeListener() {

			@Override
			public void propertyChange(final PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					progressBar.setValue((Integer) evt.getNewValue());
				}
			}

		};

		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(progressBar));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(progressBar));

		pack();
		setLocationRelativeTo(getRootPane());

		final LoadingSwingWorker swingWorker = new LoadingSwingWorker();
		swingWorker.addPropertyChangeListener(progressListener);
		swingWorker.execute();
	}

	/** A class represents swing worker for loading data. */
	private class LoadingSwingWorker extends SwingWorker<ConfigurableApplicationContext, Object> {

		/**
		 * Computes a result, or throws an exception if unable to do so.
		 *
		 * @return the computed result
		 * @throws Exception if unable to compute a result
		 */
		@Override
		protected ConfigurableApplicationContext doInBackground() throws Exception {
			final ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
			setProgress(1);
			return context;
		}

		/** Executed on the Event Dispatch Thread after the {@link #doInBackground()} method is finished. */
		@Override
		protected void done() {
			try {
				ConfigurableApplicationContext context = get();
				setVisible(false);
				dispose();
				new Catalog(context).setVisible(true);
			} catch (final InterruptedException | ExecutionException ex) {
				logger.error("Error in getting data from Swing Worker.", ex);
				System.exit(2);
			}
		}

	}

}
