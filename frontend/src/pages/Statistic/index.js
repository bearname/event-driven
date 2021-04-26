import React, { Component } from "react"
 
class Statistic extends Component {
    constructor(props) {
        super(props);
        this.state = {show: false, report: null }
    }
    
    componentDidMount() {
        const options = {
            method: 'GET',
            headers: {
              "Content-Type": "application/json",
            },
          }
        const api = `${process.env.REACT_APP_API_URL}/api/statistic/report`;
        console.log("backend: " + api)
        fetch(api, options)
        .then(response => response.json())  
        .then(data => {
            console.log(data)
            let report  = {};
            if (data.hasOwnProperty("textNumber") && data.hasOwnProperty("highRankPart") && data.hasOwnProperty("averageRank")) {
                report.textNumber = data.textNumber;
                report.highRankPart = data.highRankPart;
                report.averageRank = data.averageRank;
                this.setState({ report: report, show:true});
            }
        }) 
    }
    
    render() {
        return (
            <div>
                {this.state.show && (
                    <div>
                        <p>Text Number: {this.state.report.textNumber}</p>
                        <p>High Rank Part: {this.state.report.highRankPart}</p>
                        <p>Average Rank: {this.state.report.averageRank}</p>
                    </div>
                )}
            </div>
        )
    }
}

export default Statistic