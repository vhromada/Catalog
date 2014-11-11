goog.provide('app.Routes');

goog.require('este.Routes');

/**
 @constructor
 @extends {este.Routes}
 */
app.Routes = function () {
  this.home = this.route('/');
  this.game = this.route('/game');
};

goog.inherits(app.Routes, este.Routes);
