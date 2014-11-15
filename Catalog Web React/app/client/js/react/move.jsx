goog.provide('app.react.Move');

goog.require('goog.array');

/**
 * @constructor
 */
app.react.Move = function () {
  this.component = React.createFactory(React.createClass({
    render: function () {
      var getIndex = function (list, item) {
        return goog.array.findIndex(list, function (_item) {
          return item.id == _item.id;
        });
      };

      var index = getIndex(this.props.list, this.props.item);
      var canShow = this.props.up ? index > 0 : index < this.props.list.length - 1;

      if (canShow) {
        return <button onClick={this.props.action.bind(this, this.props.item.id)}>{this.props.text}</button>
      } else {
        return <span/>
      }
    }
  }));
};
