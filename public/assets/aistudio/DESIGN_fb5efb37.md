---
name: FinSense Mobile Design System
colors:
  surface: '#f9f9ff'
  surface-dim: '#cadaff'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f1f3ff'
  surface-container: '#e8edff'
  surface-container-high: '#e0e8ff'
  surface-container-highest: '#d7e2ff'
  on-surface: '#041b3c'
  on-surface-variant: '#3d484f'
  inverse-surface: '#1d3052'
  inverse-on-surface: '#edf0ff'
  outline: '#6d7980'
  outline-variant: '#bcc8d0'
  surface-tint: '#006686'
  primary: '#006686'
  on-primary: '#ffffff'
  primary-container: '#00baf2'
  on-primary-container: '#00465e'
  inverse-primary: '#71d2ff'
  secondary: '#3f5ba3'
  on-secondary: '#ffffff'
  secondary-container: '#96b1ff'
  on-secondary-container: '#234288'
  tertiary: '#8b5000'
  on-tertiary: '#ffffff'
  tertiary-container: '#f3972a'
  on-tertiary-container: '#603600'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#c0e8ff'
  primary-fixed-dim: '#71d2ff'
  on-primary-fixed: '#001e2b'
  on-primary-fixed-variant: '#004d66'
  secondary-fixed: '#dae1ff'
  secondary-fixed-dim: '#b3c5ff'
  on-secondary-fixed: '#001849'
  on-secondary-fixed-variant: '#25438a'
  tertiary-fixed: '#ffdcbe'
  tertiary-fixed-dim: '#ffb871'
  on-tertiary-fixed: '#2d1600'
  on-tertiary-fixed-variant: '#6a3c00'
  background: '#f9f9ff'
  on-background: '#041b3c'
  surface-variant: '#d7e2ff'
typography:
  display-currency:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 38px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 22px
    fontWeight: '700'
    lineHeight: 28px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 22px
    letterSpacing: -0.005em
  body-lg:
    fontFamily: Inter
    fontSize: 15px
    fontWeight: '400'
    lineHeight: 22px
  body-md:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
  label-caps:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.04em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 0.75rem
  margin: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 0.75rem
  space-lg: 1rem
  space-xl: 1.5rem
---

## Brand & Style

This design system powers FinSense, an intelligent financial insights and wealth intelligence experience native to the mobile consumer ecosystem. The product delivers clarity, high-speed comprehension, and instant financial confidence. 

The aesthetic is Modern Fintech Minimalism: precise, data-focused, and unmistakably authentic to the ecosystem's visual language. Content takes absolute priority over decoration. The interface creates an atmosphere of institutional security balanced with consumer-first accessibility through strict vertical hierarchy, crisp contrast ratios, and purposeful data visualization.

## Colors

The palette balances energetic interaction cues with deep, trustworthy fintech anchors.

### Core Roles
- **Primary Action (`#00BAF2`)**: Reserved for primary triggers, key progress indicators, and active tap states.
- **Deep Navy Anchor (`#002970`)**: Primary branding container, prominent figures, key statement headers, and critical high-emphasis cards.
- **Main Canvas (`#F5F7FA`)**: The base background layer across all mobile screens.
- **Surface Elevation (`#FFFFFF`)**: Pure white base for all data cards, sheets, and grouped lists.

### Typography & Structure
- **Primary Text (`#172B4D`)**: High-contrast slate navy for main figures, headings, and primary list items.
- **Secondary Text (`#68778D`)**: Neutral muted slate for labels, metadata, timestamps, and captions.
- **Structural Border (`#E8EDF3`)**: Sub-pixel and hairline dividing borders for card perimeters and nested list rows.

### Semantic Status
- **Success (`#16A36A`)**: Investment gains, completed transfers, healthy budgets.
- **Warning (`#F5A623`)**: Approaching limits, upcoming bill alerts, pending authorizations.
- **Error (`#E65353`)**: Failed transactions, negative balance warnings, critical budget overages.

## Typography

Typographic scale is built on Inter with a tight tabular layout suited for financial ledgers, transactional figures, and dense metrics.

- **Display Numbers (`display-currency`)**: For net worth, available balance, and major financial summaries. Enable tabular numbers (`tnum`) across all numeric output.
- **Labels & Overlines (`label-caps`)**: All uppercase metric tags (e.g., "SPENT THIS MONTH", "CREDIT SCORE") use 11px semi-bold with positive letter tracking.
- **Weight Pairing**: Contrast 700 weight numeric balances directly against 400/500 weight 12px secondary labels to ensure immediate glanceability on compact displays.

## Layout & Spacing

Layout geometry strictly conforms to a 390x844px mobile viewport baseline with strict edge-to-edge content boundaries.

- **Screen Margins**: Uniform 16px (`1rem`) outer margin on leading and trailing screen edges. No card or list item bleeds beyond this unless explicitly designated as a horizontal carousel.
- **Card Spacing**: Exactly 12px (`0.75rem`) vertical spacing between stacked surface cards.
- **Internal Card Density**: Standard 16px (`1rem`) padding on all internal card edges.
- **Vertical Grid Rhythm**: An 8-point baseline grid with a 4-point micro-step for tight typographic pairs (e.g., balance amount to sub-label offset is 4px).

## Elevation & Depth

This design system avoids heavy, blurred drop shadows to preserve native mobile execution speed and clean visual lines. Depth is achieved through a combination of crisp container borders and soft, directional ambient occlusion:

- **Base Surface (Level 0)**: `#F5F7FA` flat canvas.
- **Card Surface (Level 1)**: `#FFFFFF` fill with a continuous 1px outline of `#E8EDF3` and an ambient shadow: `0px 2px 6px rgba(0, 41, 112, 0.04)`.
- **Floating Controls / Bottom Sticky Trays (Level 2)**: `#FFFFFF` surface with a 1px top border of `#E8EDF3` and an elevation shadow: `0px -4px 16px rgba(0, 41, 112, 0.06)`.
- **Insight Callout Overlay (Hero Level)**: Deep Navy `#002970` surface, 0px border, with `0px 8px 20px rgba(0, 41, 112, 0.16)`.

## Shapes

Corner radii align with the modern mobile fintech standard:

- **Cards & Primary Modules**: 14px default radius (within the 12–16px specification) ensures structural integrity while softening data grids.
- **Interactive Action Buttons**: 12px corner radius for full 48px height triggers.
- **Badges & Filter Chips**: Fully rounded pill shapes (`999px`) to maintain clear shape distinction against rectangular metric cards.
- **Input Fields**: 12px corner radius matching primary action buttons.

## Components

### Buttons
- **Height**: Fixed 48px height across all standard interactions for optimal touch ergonomics.
- **Primary**: Background `#00BAF2`, text `#FFFFFF`, font `label-lg`, border-radius 12px. Active tap scale down to 0.98.
- **Secondary (Institutional)**: Background `#002970`, text `#FFFFFF`, font `label-lg`, border-radius 12px.
- **Tertiary / Ghost**: Transparent fill, 1px `#E8EDF3` border, text `#002970`, border-radius 12px.

### Cards
- **Standard Insight Card**: `#FFFFFF` background, 16px internal padding, 14px border radius, 1px solid `#E8EDF3`. Separated vertically by 12px.
- **Hero Balance Card**: Optional `#002970` fill with `#FFFFFF` primary text and `#00BAF2` graphic accents. 16px padding, 16px border radius.

### Input Fields
- **Container**: 48px height, `#FFFFFF` background, 1px solid `#E8EDF3`, 12px border radius, 12px horizontal padding.
- **Focus State**: Border color transitions to `#00BAF2` with a 1px inner glow. Placeholder text in `#68778D`.

### Filter Chips
- **Geometry**: Height 32px, 8px horizontal padding, pill shape (999px radius).
- **Default State**: Background `#FFFFFF`, 1px solid `#E8EDF3`, text `#172B4D` (12px, font weight 500).
- **Selected State**: Background `#E6F8FE`, border 1px solid `#00BAF2`, text `#002970` (12px, font weight 600).

### Form Elements (Checkboxes & Radios)
- **Checkboxes**: 20x20px square with a 4px corner radius. Inactive: `#E8EDF3` border with `#FFFFFF` fill. Active: `#00BAF2` solid fill with centered `#FFFFFF` check vector.
- **Radio Buttons**: 20x20px circular ring. Active: `#00BAF2` outer ring with an internal 10px `#00BAF2` centered dot.

### List Items & Transaction Rows
- **Layout**: 56px minimum row height, horizontal flex layout with icon/avatar (40px) at the start, two-line text group (15px `#172B4D` title + 12px `#68778D` subtitle), and trailing numeric block (15px tabular `#172B4D` + status indicator).
- **Dividers**: 1px solid `#E8EDF3` inset by 56px from the leading edge to align with the text group.

### Financial Indicator Badges
- **Structure**: Inline pill, 20px height, 6px horizontal padding.
- **Gain**: Background `#E8F8F1`, text `#16A36A`, 11px font weight 600 with prefix `+`.
- **Loss / Expense**: Background `#FDF0F0`, text `#E65353`, 11px font weight 600 with prefix `-`.