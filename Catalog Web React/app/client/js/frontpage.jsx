goog.provide('app.FrontPage');

/**
 @param {Element} element
 @param {app.Dispatcher} dispatcher
 @param {app.react.App} reactApp
 @constructor
 */
app.FrontPage = function (element, dispatcher, reactApp) {
  this.element = element;
  this.dispatcher = dispatcher;
  this.reactApp = reactApp;
};

app.FrontPage.prototype.init = function () {
  return this.dispatcherId = this.dispatcher.register((function (page) {
    return function (action, payload) {
      switch (action) {
        case app.Actions.SYNC_VIEW:
          React.render(<page.reactApp.component/>, page.element);
      }
    };
  })(this));
};
