
function plotValues(plottingArrays){
    const data1 = [
      {
        x: plottingArrays[0],
        y: plottingArrays[1],
        mode: "markers",
        name: "Interest Monthly Payment",
      },
      {
        x: plottingArrays[0],
        y: plottingArrays[2],
        mode: "markers",
        name: "Principal Monthly Payment",
      },
    ];
    const data2 = [
        {
            x: plottingArrays[0],
            y: plottingArrays[3],
            mode: "markers",
            name: "Remaining Balance",
        },
        ];
    const layout1 = {
        title: "Mortgage Payments",
        xaxis: {
            title: "Number of Months",
        },
        yaxis: {
            title: "Dollars ($)",
        },
        };
    const layout2 = {
        title: "Mortgage Payments",
        xaxis: {
            title: "Number of Months",
        },
        yaxis: {
            title: "Dollars ($)",
        },
        };
    Plotly.newPlot("monthlyInterestRateAndPrincipalPayments", data1, layout1);
    Plotly.newPlot("monthlyPrincipalValues", data2, layout2);

}
function getLoanInformation() {
    let housePrice = prompt("Enter the house price: ");
    let downPayment = prompt("Enter the down payment: ");
    let annualInterestRate = prompt("Enter the annual interest rate: ");
    let loanPeriodInYears = prompt("Enter the loan period in years: ");
    return [housePrice, downPayment, annualInterestRate, loanPeriodInYears];
}

function getLoanPaymentValues(principal, monthlyPayment, annualInterestRate, loanPeriod) {
    let interestMonthlyPayments = [];
    let principalMonthlyPayments = [];
    let remainingBalance = principal;
    let remainingBalanceArray = [];
    let monthlyInterestRate = annualInterestRate / 100 / 12;
    let totalNumberOfPayments = loanPeriod * 12;
    let paymentNumArray = Array.from({length: totalNumberOfPayments}, (_, i) => i + 1);
    
    for (let i = 0; i < totalNumberOfPayments; i++) {
        let interestPayment = remainingBalance * monthlyInterestRate;
        let principalPayment = monthlyPayment - interestPayment;
        remainingBalance -= principalPayment;
        remainingBalanceArray.push(remainingBalance);
        interestMonthlyPayments.push(interestPayment);
        principalMonthlyPayments.push(principalPayment);
    }

    return [paymentNumArray, interestMonthlyPayments, principalMonthlyPayments, remainingBalance];
}

function plotMortgageCurves(principal, annualInterestRate, monthlyPayment, loanPeriod) {
  let plottingArrays = getLoanPaymentValues(principal, monthlyPayment, annualInterestRate, loanPeriod); // see note #6
  plotValues(plottingArrays); 
}

function computeMontlyMortgagePayments(principal_P, montlyInterestRate_r, totalNumberOfPayments_n) {
  return (
    (principal_P * (montlyInterestRate_r * Math.pow(1 + montlyInterestRate_r, totalNumberOfPayments_n))) /
    (Math.pow(1 + montlyInterestRate_r, totalNumberOfPayments_n) - 1)
  );
}

function displayResults(housePrice, downPayment, annualInterestRate, loanPeriodInYears, monthlyMortgagePayments_M) {
    document.write(`<h1>Mortgage Calculator</h1>`)
    document.write(`<h2>House Price: $${housePrice}</h2>`);
    document.write(`<h2>Down Payment: $${downPayment}</h2>`);
    document.write(`<h2>Pricipal: $${housePrice - downPayment}</h2>`);
    document.write(`<h2>Annual Interest Rate: ${annualInterestRate}%</h2>`);
    document.write(`<h2>Loan Period: ${loanPeriodInYears} years</h2>`);
    document.write(`<h2>Monthly Mortgage Payment: $${monthlyMortgagePayments_M.toFixed(2)} per month</h2>`);
    document.write(`<h2>Minimum Monthly Income: $${((monthlyMortgagePayments_M * 12) / 0.3).toFixed(2)} per year</h2>`);
}

function main_driver() {
  let loanInformation = getLoanInformation();
  let housePrice = loanInformation[0];
  let downPayment = loanInformation[1];
  let annualInterestRate = loanInformation[2];
  let loanPeriodInYears = loanInformation[3];

  let principal_P = housePrice - downPayment;  
  let montlyInterestRate_r = annualInterestRate / 100 / 12;  
  let totalNumberOfPayments_n = loanPeriodInYears * 12;  
  let monthlyMortgagePayments_M = computeMontlyMortgagePayments( 
    principal_P,
    montlyInterestRate_r,
    totalNumberOfPayments_n
  );
  displayResults(housePrice, downPayment, annualInterestRate, loanPeriodInYears, monthlyMortgagePayments_M); 
  plotMortgageCurves(principal_P, annualInterestRate, monthlyMortgagePayments_M, loanPeriodInYears); 
}
