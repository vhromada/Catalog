goog.provide('app.react.Menu');

/**
 * @param {app.Routes} routes
 * @constructor
 */
app.react.Menu = function (routes) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <nav>
          <ul id="menu">
            <li>
              <a href={routes.home.url()}>Catalog</a>
            </li>
            <li>
              <a href={routes.games.url()}>Games</a>
              <ul>
                <li>
                  <a href={routes.games.url()}>All games</a>
                  <a href={routes.addGame.url()}>Add game</a>
                </li>
              </ul>
            </li>
          </ul>
        </nav>
      );
    }
  }));
};
