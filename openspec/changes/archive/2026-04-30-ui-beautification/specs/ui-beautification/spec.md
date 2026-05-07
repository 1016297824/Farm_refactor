## ADDED Requirements

### Requirement: UI Beautification System
The UI beautification system SHALL provide consistent styling across all frontend components using the established design system tokens.

#### Scenario: Design system tokens applied
- **WHEN** any Vue component renders
- **THEN** it SHALL use CSS variables from myCSS.css for colors, shadows, and spacing

#### Scenario: Navbar components styled
- **WHEN** any Navbar component renders
- **THEN** it SHALL use `.farm-navbar-*` classes with gradient backgrounds and shadow
- **AND** dropdown menus SHALL animate on hover

#### Scenario: AlertDialog shows toast notifications
- **WHEN** bus.$emit(bus.error, message) is called
- **THEN** a styled Toast notification SHALL appear
- **AND** it SHALL auto-dismiss after 3 seconds

#### Scenario: Business pages use card layout
- **WHEN** any business page renders
- **THEN** content containers SHALL use `.farm-card` class
- **AND** buttons SHALL use `.farm-btn*` classes

#### Scenario: Forms use styled inputs
- **WHEN** form inputs are rendered
- **THEN** they SHALL use `.farm-input` class
- **AND** labels SHALL use `.farm-label` class

#### Scenario: Tables use styled layout
- **WHEN** data tables are rendered
- **THEN** they SHALL use `.farm-table` class
- **AND** pagination SHALL use `.farm-pagination*` classes

### Requirement: Bootstrap Modal Replacement
All Bootstrap modal dialogs SHALL be replaced with the farm-modal design system to remove jQuery dependency and provide consistent modal styling.

#### Scenario: Modals use farm-modal design system
- **WHEN** any business page uses modal dialogs
- **THEN** modals SHALL use `farm-modal-overlay` + `farm-modal` classes instead of Bootstrap modal markup
- **AND** modal visibility SHALL be controlled by `v-if` reactive data instead of jQuery `data-toggle`/`data-target`
- **AND** clicking the overlay backdrop SHALL close the modal

#### Scenario: Confirm dialogs replaced
- **WHEN** a delete or destructive action requires confirmation
- **THEN** `confirm()` native dialogs SHALL be replaced with custom confirm dialogs using `farm-modal-overlay`
- **AND** the confirm dialog SHALL display the item name being deleted
- **AND** the confirm dialog SHALL have cancel and confirm buttons

### Requirement: RestaurantMaterial Module Migration
The restaurant material management feature SHALL be located under the restaurant staff module, not the farm staff module, as it manages restaurant-specific inventory.

#### Scenario: Component location
- **WHEN** the restaurant material management feature is accessed
- **THEN** the Vue component SHALL be at `views/staff/restaurantStaff/RestaurantMaterial.vue`
- **AND** the route SHALL be `/restaurantStaff/restaurantMaterial`

#### Scenario: API endpoints
- **WHEN** restaurant material API calls are made
- **THEN** the API SHALL call `/api/restaurantStaff/*` endpoints (not `/api/farmStaff/*`)
- **AND** the backend endpoints SHALL be in `RestaurantStaffController` (not `FarmStaffController`)

#### Scenario: Navigation updated
- **WHEN** the restaurant staff navbar renders
- **THEN** it SHALL include the "餐厅物资" link
- **WHEN** the farm staff navbar renders
- **THEN** it SHALL NOT include the "餐厅物资采购" link

### Requirement: Product Produce Bug Fix
When producing poultry products, both the stock amount and raw material amount SHALL be incremented so the stock change is visible in the product table.

#### Scenario: Poultry produce increments stock
- **WHEN** a farm staff produces poultry products
- **THEN** both `amount` (stock) and `baseAmount` (raw material stock) SHALL be incremented
- **AND** the stock change SHALL be visible in the product table immediately
