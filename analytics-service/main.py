from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List
import pandas as pd
import numpy as np
from datetime import datetime

from sklearn.linear_model import LinearRegression

app = FastAPI(title="Cloud Financial Analytics Service")

### Reuqest calculations
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  ### CHANGE THIS LATER TO JUST API HOST
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


### Validate transactions
class TransactionPayload(BaseModel):
    date: str
    description: str
    category: str
    amount: float  ### Negative for expense, positive for income


@app.get("/health")
def health_check():
    return {"status": "healthy"}


@app.post("/analytics/spending")
def get_spending_analytics(transactions: List[TransactionPayload]):
    pass


@app.post("/analytics/categories")
def get_category_analytics(transactions: List[TransactionPayload]):
    pass


@app.post("/analytics/monthly")
def get_monthly_forecast(transactions: List[TransactionPayload]):
    pass


def helper_build_dataframe(transactions: List[TransactionPayload]) -> pd.DataFrame:
    """Utility function to parse transactions into a structured Pandas DataFrame."""
    if not transactions:
        raise HTTPException(status_code=400, detail="Transaction log is empty")

    ### Convert models to a list
    data = [t.model_dump() for t in transactions]
    df = pd.DataFrame(data)

    ### Use correct data types
    df["date"] = pd.to_datetime(df["date"])
    df["amount"] = pd.to_numeric(df["amount"])
    return df


### Spending analytics endpoints
@app.post("/analytics/spending")
def get_spending_analytics(transactions: List[TransactionPayload]):
    df = helper_build_dataframe(transactions)

    ### Show  expenses only
    expenses_df = df[df["amount"] < 0].copy()
    expenses_df["abs_amount"] = expenses_df["amount"].abs()

    if expenses_df.empty:
        return {"monthly_spending": 0, "average_daily": 0, "largest_category": "None"}

    ### Get total spendings
    total_spending = expenses_df["abs_amount"].sum()

    ### Average daily spendings
    days_range = (expenses_df["date"].max() - expenses_df["date"].min()).days + 1
    average_daily = total_spending / days_range if days_range > 0 else total_spending

    ### Get the category with the largest amoutn spent
    category_totals = expenses_df.groupby("category")["abs_amount"].sum()
    largest_category = category_totals.idxmax() if not category_totals.empty else "None"

    return {
        "monthly_spending": float(np.round(total_spending, 2)),
        "average_daily": float(np.round(average_daily, 2)),
        "largest_category": largest_category,
    }


### Category endpoints
@app.post("/analytics/categories")
def get_category_analytics(transactions: List[TransactionPayload]):
    df = helper_build_dataframe(transactions)
    expenses_df = df[df["amount"] < 0]

    if expenses_df.empty:
        return {}

    ### Group by category and amount
    category_breakdown = expenses_df.groupby("category")["amount"].sum().abs()

    ### Convert index to a  dictionary response
    return {
        category: float(np.round(value, 2))
        for category, value in category_breakdown.items()
    }


### Monthly prediction endpoints
@app.post("/analytics/monthly")
def get_monthly_forecast(transactions: List[TransactionPayload]):
    df = helper_build_dataframe(transactions)
    expenses_df = df[df["amount"] < 0].copy()

    if expenses_df.empty:
        return {"predicted_next_month_spending": 0.0, "confidence": "insufficient_data"}

    ### Group expenses by
    expenses_df["year_month"] = expenses_df["date"].dt.to_period("M")
    monthly_data = expenses_df.groupby("year_month")["amount"].sum().abs().reset_index()

    #### Trend line to show trajectory
    if len(monthly_data) < 2:
        ### If there isn't a lot of data history, a baseline amount is created
        fallback_prediction = expenses_df["amount"].sum() / len(monthly_data)
        return {
            "predicted_next_month_spending": float(
                np.round(abs(fallback_prediction), 2)
            ),
            "note": "Baseline average applied. Gather more data to enable trend line regressions.",
        }

    ### Change into a array index for time steps
    monthly_data["time_step"] = np.arange(len(monthly_data))

    ### Define features and target outputs
    X = monthly_data[["time_step"]].values
    y = monthly_data["amount"].values

    ### Train the Linear Regression Model
    model = LinearRegression()
    model.fit(X, y)

    ### Predict the next time step value
    next_time_step = np.array([[len(monthly_data)]])
    predicted_spending = model.predict(next_time_step)[0]

    ### Stop model from predicting negative amounts of spending
    final_forecast = max(0.0, predicted_spending)

    return {
        "predicted_next_month_spending": float(np.round(final_forecast, 2)),
        "historical_months_analyzed": len(monthly_data),
    }
