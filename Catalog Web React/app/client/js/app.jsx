goog.provide('App');

/**
 * @param {Element} element
 * @param {app.Dispatcher} dispatcher
 * @param {app.react.App} reactApp
 * @param {app.routes.Store} routesStore
 * @param {app.stores.StoreRegistry} registry
 * @constructor
 */
var App = function (element, dispatcher, reactApp, routesStore, registry) {
  registry.listen('change', function () {
    dispatcher.dispatch(app.Actions.RENDER_APP)
  });

  dispatcher.register(function (action, payload) {
    switch (action) {
      case app.Actions.RENDER_APP:
        return React.render(<reactApp.component/>, element);
    }
  });

  routesStore.start();
};
