var chart = document.getElementById("DeliveriesPerDay");
console.log(chart);
let barchart = new Chart(chart, {
	type: 'horizontalBar',
	data: {
		labels: ["Monday", "Tuesday", "Wednesday", "Thirsday", "Friday"],
		datasets: [
			{
				label: "Deliveries Per Day",
				fill: true,
				backgroundColor:[
					"rgba(255, 99, 132, 0.2)",
					"rgba(255, 159, 64, 0.2)",
					"rgba(255, 205, 86, 0.2)",
					"rgba(75, 192, 192, 0.2)",
					"rgba(75, 50, 60, 0.2)"],
				borderColor:[
					"rgb(255, 99, 132)",
					"rgb(255, 159, 64)",
					"rgb(255, 205, 86)",
					"rgb(75, 192, 192)",
					"rgb(54, 162, 235)"],
				borderWidth: 1,
				data: [65,59,80,81,56,55,40]
			},
			{
				label: "Deliveries On Time",
				fill: true,
				backgroundColor:[
					"rgba(255, 99, 132, 0.5)",
					"rgba(255, 159, 64, 0.5)",
					"rgba(255, 205, 86, 0.5)",
					"rgba(75, 192, 192, 0.5)",
					"rgba(75, 50, 60, 0.5)"],
				borderColor:[
					"rgb(255, 99, 132)",
					"rgb(255, 159, 64)",
					"rgb(255, 205, 86)",
					"rgb(75, 192, 192)",
					"rgb(54, 162, 235)"],
				borderWidth: 1,
				data: [35,29,40,21,36,25,10]				
			}
		]
	},
	options: {
		responsive: true,
		maintainAspectRatio: false,
		scales: {
			yAxes: [{
				ticks: {
					beginAtZero: true
				}
			}]
		}
	}
});