goog.provide('app.FrontPage');

/**
 @param {Element} element
 @param {app.Dispatcher} dispatcher
 @param {app.react.App} reactApp
 @param {app.stores.StoreRegistry} registry
 @constructor
 */
app.FrontPage = function (element, dispatcher, reactApp, registry) {
  this.element = element;
  this.dispatcher = dispatcher;
  this.reactApp = reactApp;
  this.registry = registry;
};

app.FrontPage.prototype.init = function () {
  this.registry.listen('change', function () {
    this.dispatcher.dispatch(app.Actions.SYNC_VIEW)
  }.bind(this));

  return this.dispatcherId = this.dispatcher.register((function (page) {
    return function (action, payload) {
      switch (action) {
        case app.Actions.SYNC_VIEW:
          React.render(<page.reactApp.component/>, page.element);
      }
    };
  })(this));
};
