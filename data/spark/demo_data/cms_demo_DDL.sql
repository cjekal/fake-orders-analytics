USE [CMS_DEMO]
GO
/****** Object:  Table [dbo].[beneficiary]    Script Date: 5/27/2021 3:40:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[beneficiary](
	[beneficiary_id] [varchar](9) NOT NULL,
	[reference_year] [smallint] NULL,
	[state] [varchar](225) NULL,
	[county] [varchar](50) NULL,
	[gender] [varchar](225) NULL,
	[race] [varchar](225) NULL,
	[age] [smallint] NULL,
	[reason_for_entitlement] [varchar](500) NULL,
	[date_of_death] [smalldatetime] NULL,
	[medicare_medicaid_status] [varchar](225) NULL,
 CONSTRAINT [PK_beneficiary] PRIMARY KEY CLUSTERED 
(
	[beneficiary_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[claim]    Script Date: 5/27/2021 3:40:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[claim](
	[beneficiary_id] [varchar](9) NULL,
	[claim_date] [smalldatetime] NULL,
	[claim_number] [bigint] NOT NULL,
	[claim_type] [varchar](225) NULL,
	[disposition] [varchar](50) NULL,
	[carrier_number] [varchar](5) NULL,
	[payment_denial_code] [varchar](2) NULL,
	[claim_payment_amount] [numeric](12, 2) NULL,
	[referring_physician_npi] [varchar](12) NULL,
	[referring_physician_upin] [varchar](12) NULL,
	[provider_payment_amount] [numeric](12, 2) NULL,
	[beneficiary_payment_amount] [numeric](12, 2) NULL,
	[claim_submitted_amount] [numeric](12, 2) NULL,
	[claim_allowed_amount] [numeric](12, 2) NULL,
	[claim_cash_deductible_applied_amount] [numeric](12, 2) NULL,
	[principal_diagnosis_code] [varchar](7) NULL,
	[principal_diagnosis] [varchar](512) NULL,
	[diagnosis_code] [varchar](107) NULL,
	[diagnosis] [varchar](6167) NULL,
	[age_range] [varchar](225) NULL,
	[gender] [varchar](225) NULL,
	[race] [varchar](225) NULL,
	[county] [varchar](50) NULL,
	[state] [varchar](225) NULL,
PRIMARY KEY CLUSTERED 
(
	[claim_number] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[claim_service]    Script Date: 5/27/2021 3:40:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[claim_service](
	[beneficiary_id] [varchar](9) NULL,
	[claim_number] [bigint] NOT NULL,
	[line_number] [smallint] NOT NULL,
	[claim_date] [smalldatetime] NULL,
	[claim_type] [varchar](50) NULL,
	[performing_pin_number] [varchar](15) NULL,
	[performing_physician_npi] [varchar](12) NULL,
	[provider_type] [varchar](50) NULL,
	[provider_state] [varchar](225) NULL,
	[provider_specialty] [varchar](5000) NULL,
	[cms_type_service] [varchar](500) NULL,
	[place_of_service] [varchar](50) NULL,
	[last_expense_date] [smalldatetime] NULL,
	[hcpcs_code] [varchar](5) NULL,
	[payment_amount] [numeric](12, 2) NULL,
	[bene_payment_amount] [numeric](12, 2) NULL,
	[provider_payment_amount] [numeric](12, 2) NULL,
	[bene_part_b_deductible_amount] [numeric](12, 2) NULL,
	[bene_primary_payer_paid_amount] [numeric](12, 2) NULL,
	[coinsurance_amount] [numeric](12, 2) NULL,
	[submitted_charged_amount] [numeric](12, 2) NULL,
	[allowed_charged_amount] [numeric](12, 2) NULL,
	[diagnosis_code] [varchar](7) NULL,
	[diagnosis] [varchar](512) NULL,
	[clinical_lab_charge_amount] [numeric](12, 2) NULL,
	[other_applied_amount1] [numeric](12, 2) NULL,
	[other_applied_amount2] [numeric](12, 2) NULL,
	[other_applied_amount3] [numeric](12, 2) NULL,
	[other_applied_amount4] [numeric](12, 2) NULL,
	[other_applied_amount5] [numeric](12, 2) NULL,
	[other_applied_amount6] [numeric](12, 2) NULL,
	[other_applied_amount7] [numeric](12, 2) NULL
) ON [PRIMARY]

GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [idx_claim_beneficiary_id]    Script Date: 5/27/2021 3:40:50 PM ******/
CREATE NONCLUSTERED INDEX [idx_claim_beneficiary_id] ON [dbo].[claim]
(
	[beneficiary_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [idx_claim_service_beneficiary_id]    Script Date: 5/27/2021 3:40:50 PM ******/
CREATE NONCLUSTERED INDEX [idx_claim_service_beneficiary_id] ON [dbo].[claim_service]
(
	[beneficiary_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [idx_claim_service_claim_number_line]    Script Date: 5/27/2021 3:40:50 PM ******/
CREATE NONCLUSTERED INDEX [idx_claim_service_claim_number_line] ON [dbo].[claim_service]
(
	[claim_number] ASC,
	[line_number] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[claim]  WITH CHECK ADD  CONSTRAINT [FK_claim_beneficiary] FOREIGN KEY([beneficiary_id])
REFERENCES [dbo].[beneficiary] ([beneficiary_id])
GO
ALTER TABLE [dbo].[claim] CHECK CONSTRAINT [FK_claim_beneficiary]
GO
ALTER TABLE [dbo].[claim_service]  WITH CHECK ADD  CONSTRAINT [FK_claim_service_beneficiary] FOREIGN KEY([beneficiary_id])
REFERENCES [dbo].[beneficiary] ([beneficiary_id])
GO
ALTER TABLE [dbo].[claim_service] CHECK CONSTRAINT [FK_claim_service_beneficiary]
GO
ALTER TABLE [dbo].[claim_service]  WITH CHECK ADD  CONSTRAINT [FK_claim_service_claim] FOREIGN KEY([claim_number])
REFERENCES [dbo].[claim] ([claim_number])
GO
ALTER TABLE [dbo].[claim_service] CHECK CONSTRAINT [FK_claim_service_claim]
GO
