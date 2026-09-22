import pytest
from main import TransactionPayload, get_spending_analytics, get_monthly_forecast


@pytest.fixture
def sample_transactions():
    return [
        TransactionPayload(
            date="2026-09-02", description="Tesco", category="Food", amount=-45.0
        ),
        TransactionPayload(
            date="2026-09-03", description="Salary", category="Income", amount=2500.0
        ),
        TransactionPayload(
            date="2026-09-04", description="Bus", category="Transport", amount=-15.0
        ),
    ]


def test_calculate_spending_and_averages(sample_transactions):
    result = get_spending_analytics(sample_transactions)

    assert result["monthly_spending"] == 60.0
    assert result["largest_category"] == "Food"
    ### Gets the average spending average assuming 3 days of transaction
    assert result["average_daily"] == 20.0


def test_forecast_spending_insufficient_data():
    single_tx = [
        TransactionPayload(
            date="2026-09-02", description="Tesco", category="Food", amount=-45.0
        )
    ]
    result = get_monthly_forecast(single_tx)

    assert "predicted_next_month_spending" in result
    assert "note" in result
