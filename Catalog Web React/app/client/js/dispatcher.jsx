goog.provide('app.Dispatcher');

goog.require('este.Dispatcher');

/**
 * @constructor
 * @extends {este.Dispatcher}
 */
app.Dispatcher = function () {
  app.Dispatcher.superClass_.constructor.call(this);
};

goog.inherits(app.Dispatcher, este.Dispatcher);
