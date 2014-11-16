goog.provide('App');

/**
 * @param {Element} element
 * @param {app.Dispatcher} dispatcher
 * @param {app.react.App} reactApp
 * @param {app.routes.Store} routesStore
 * @constructor
 */
var App = function (element, dispatcher, reactApp, routesStore) {
  dispatcher.register(function (action, payload) {
    switch (action) {
      case app.Actions.RENDER_APP:
        return React.render(<reactApp.component/>, element);
    }
  });

  routesStore.start();
};
