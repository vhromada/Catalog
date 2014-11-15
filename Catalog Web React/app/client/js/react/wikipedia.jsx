goog.provide('app.react.Wikipedia');

/**
 * @constructor
 */
app.react.Wikipedia = function () {
  this.component = React.createFactory(React.createClass({
    render: function () {
      if (goog.string.isEmptyOrWhitespaceSafe(this.props.url)) {
        return <span/>
      } else {
        return <a href={this.wikiUrl(this.props.country, this.props.url)} target="_blank">{this.props.text}</a>
      }
    },
    wikiUrl: function (country, url) {
      return 'http://' + country + '.wikipedia.org/wiki/' + url;
    }
  }));
};
