goog.provide('app.Routes');

goog.require('este.Routes');

/**
 * @constructor
 * @extends {este.Routes}
 */
app.Routes = function () {
  goog.base(this);

  this.home = this.route('/');
  this.games = this.route('/games');
  this.addGame = this.route('/games/add');
};

goog.inherits(app.Routes, este.Routes);
